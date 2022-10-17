package com.crewmeister.cmcodingchallenge.services.exchange.impl;

import com.crewmeister.cmcodingchallenge.validators.DateValidator;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.services.HTMLParser;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateFinder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ExchangeRateFinderImpl implements ExchangeRateFinder {
  private static Logger logger = LoggerFactory.getLogger(ExchangeRateFinderImpl.class);

  @Value("${exchange.service.url}")
  private String url;

  @Override
  public Set<ExchangeRate> getExchangeRatesForAllDates(String symbol) {
    Document document = HTMLParser.getDocument(url);
    Element element = document.getElementsByTag("tbody").get(0);

    Optional<Node> node = element.childNodes()
            .stream().filter( (x) ->
              !x.childNodes().isEmpty() && x.childNodes().get(3).childNodes().get(0).attr("text").contains(symbol)
            ).findAny();

    if (node.isPresent()) {
      String href = node.get().childNodes().get(5).childNodes().get(1).attributes().get("href");
      return fetchExchangeRateFromCSVForAllDate(href);
    } else {
      logger.info("Unable to fetch exchange rates as Currency not supported");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found for currency");
    }
  }

  @Override
  public ExchangeRate getExchangeRateForDate(String symbol, String date) {
    Document document = HTMLParser.getDocument(url);
    Element element = document.getElementsByTag("tbody").get(0);

    Optional<Node> node = element.childNodes()
            .stream().filter( (x) ->
                    !x.childNodes().isEmpty() && x.childNodes().get(3).childNodes().get(0).attr("text").contains(symbol)
            ).findAny();

    if (node.isPresent()) {
      String href = node.get().childNodes().get(5).childNodes().get(1).attributes().get("href");
      return  fetchExchangeRateFromCSVForDate(href, date);
    } else {
       throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found for currency");
    }
  }

  private ExchangeRate fetchExchangeRateFromCSVForDate(String csvUrl, String date) {
    try {
      URL url = new URL(csvUrl);
      CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
      CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
      DateValidator dateValidator = new DateValidator(DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
              .withResolverStyle(ResolverStyle.STRICT));
      for(CSVRecord csvRecord : csvParser) {
        String csvDate = csvRecord.get(0);
        if (dateValidator.isValid(csvDate) && csvDate.equals(date)) {
          String value = !csvRecord.get(1).isEmpty() && Pattern.compile("-?\\d+(\\.\\d+)?").matcher(csvRecord.get(1)).matches() ? csvRecord.get(1) : "0";
          return new ExchangeRate(LocalDate.parse(date), BigDecimal.valueOf(Double.parseDouble(value)));
        }
      }
      return  null;
    } catch (IOException e) {
      logger.error("Error while parsing CSV", e.getMessage());
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error while parsing CSV");
    }
  }

  private Set<ExchangeRate> fetchExchangeRateFromCSVForAllDate(String csvUrl) {
    Set<ExchangeRate> exchangeRateList = new HashSet<>();
    try {
      URL url = new URL(csvUrl);
      CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
      CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
      DateValidator dateValidator = new DateValidator(DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
              .withResolverStyle(ResolverStyle.STRICT));
      for(CSVRecord csvRecord : csvParser) {
        String date = csvRecord.get(0);
        if (dateValidator.isValid(date)) {
          String value = !csvRecord.get(1).isEmpty() && Pattern.compile("-?\\d+(\\.\\d+)?").matcher(csvRecord.get(1)).matches() ? csvRecord.get(1) : "0";
          exchangeRateList.add(new ExchangeRate(LocalDate.parse(date), BigDecimal.valueOf(Double.parseDouble(value))));
        }
      }
      return  exchangeRateList;
    } catch (IOException e) {
      logger.error("Error while parsing CSV", e.getMessage());
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error while parsing CSV");
    }
  }
}
