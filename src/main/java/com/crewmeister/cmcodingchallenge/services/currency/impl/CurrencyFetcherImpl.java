package com.crewmeister.cmcodingchallenge.services.currency.impl;

import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.services.HTMLLoader;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyFetcherImpl implements CurrencyFetcher {
  private static Logger logger = LoggerFactory.getLogger(CurrencyFetcherImpl.class);

  @Value("${exchange.service.url}")
  private String url;

  private List<Currency> currencies;

  public CurrencyFetcherImpl() {
      this.currencies = new ArrayList<Currency>();
  }

  @Override
  public List<Currency> loadCurrencies() {

    Document document = HTMLLoader.getDocument(url);
    Element element = document.getElementsByTag("tbody").get(0);
    List<Currency> formattedCurrencies = element.childNodes()
            .stream().filter( (x) -> !x.childNodes().isEmpty()).skip(1).map( (x) -> {
      String text = x.childNodes().get(3).childNodes().get(0).attr("text");
      String[] countryAndSymbol = text.split("=")[1].split(".../");
      String symbol = countryAndSymbol[0].trim().split(" ")[0];
      String country = countryAndSymbol[1].trim();
      return new Currency(symbol, country);
    }).collect(Collectors.toList());

    this.currencies = formattedCurrencies;
    return this.currencies;
  }
}
