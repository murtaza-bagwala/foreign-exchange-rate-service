package com.crewmeister.cmcodingchallenge.services.exchange;

import com.crewmeister.cmcodingchallenge.models.ExchangeRate;

import java.util.Set;

public interface ExchangeRateService {
  ExchangeRate fetchExchangeRateForDate(String symbol, String date);

  Set<ExchangeRate> fetchExchangeRateForAllDate(String symbol);
}
