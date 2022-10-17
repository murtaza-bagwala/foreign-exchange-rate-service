package com.crewmeister.cmcodingchallenge.services.exchange.impl;

import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repositories.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repositories.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateFinder;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
  private static Logger logger = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

  @Autowired
  private ExchangeRateFinder exchangeRateFinder;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private ExchangeRateRepository exchangeRateRepository;

  @Override
  public ExchangeRate fetchExchangeRateForDate(String symbol, String date) {
    Optional<Currency> currency = currencyRepository.findBySymbol(symbol);
    ExchangeRate exchangeRate = null;

    if (currency.isPresent()) {
      Optional<ExchangeRate> exchangeRateFromDB = exchangeRateRepository.findByCurrencyAndDate(currency.get(), LocalDate.parse(date));
      if (exchangeRateFromDB.isPresent()) {
        exchangeRate = exchangeRateFromDB.get();
      } else {
        exchangeRate = exchangeRateFinder.getExchangeRateForDate(symbol, date);
        exchangeRateRepository.save(exchangeRate);
        return exchangeRate;
      }
      return exchangeRate;
    } else {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Currency not found");
    }
  }

  @Override
  public Set<ExchangeRate> fetchExchangeRateForAllDate(String symbol) {
    Optional<Currency> currency = currencyRepository.findBySymbol(symbol);

    if (currency.isPresent()) {
      Currency currencyFromDB = currency.get();
      Set<ExchangeRate> exchangeRateList = exchangeRateFinder.getExchangeRatesForAllDates(symbol).stream()
              .map((x) -> {
                x.setCurrency(currency.get());
                return x;
              }).collect(Collectors.toSet());
      currencyFromDB.setExchangeRates((exchangeRateList));
      exchangeRateRepository.saveAll(exchangeRateList);
      currencyRepository.save(currencyFromDB);
      return exchangeRateList;
    } else {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Currency not found");
    }
  }
}
