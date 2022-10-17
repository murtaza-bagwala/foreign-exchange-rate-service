package com.crewmeister.cmcodingchallenge.services.currency.impl;

import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.repositories.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyFetcher;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

  @Autowired
  private CurrencyFetcher currencyFetcher;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Override
  public List<Currency> fetchCurrencies() {

    List<Currency> currencies = currencyRepository.findAll();
    if (currencies.isEmpty()) {
      currencies = currencyFetcher.loadCurrencies();
      currencyRepository.saveAll(currencies);
    }
    return currencies;
  }
}
