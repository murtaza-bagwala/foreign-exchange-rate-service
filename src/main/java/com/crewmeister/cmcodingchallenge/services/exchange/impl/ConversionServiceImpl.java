package com.crewmeister.cmcodingchallenge.services.exchange.impl;

import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repositories.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repositories.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.services.exchange.ConversionService;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ConversionServiceImpl implements ConversionService {
  private static Logger logger = LoggerFactory.getLogger(ConversionServiceImpl.class);

  @Autowired
  private ExchangeRateFinder exchangeRateFinder;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private ExchangeRateRepository exchangeRateRepository;

  @Override
  public ConvertedAmount getConvertedAmount(String symbol, String date, double amount) {
    Optional<Currency> currency = currencyRepository.findBySymbol(symbol);
    ExchangeRate exchangeRate = null;

    if (currency.isPresent()) {
      Optional<ExchangeRate> exchangeRateFromDB = exchangeRateRepository.findByCurrencyAndDate(currency.get(), LocalDate.parse(date));
      if (exchangeRateFromDB.isPresent()) {
        return new ConvertedAmount(amount, exchangeRateFromDB.get().getValue());
      } else {
        exchangeRate = exchangeRateFinder.getExchangeRateForDate(symbol, date);
        exchangeRateRepository.save(exchangeRate);
        return new ConvertedAmount(amount, exchangeRate.getValue());
      }
    } else {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
  }
}
