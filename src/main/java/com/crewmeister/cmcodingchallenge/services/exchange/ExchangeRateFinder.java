package com.crewmeister.cmcodingchallenge.services.exchange;

import com.crewmeister.cmcodingchallenge.models.ExchangeRate;

import java.util.Set;

public interface ExchangeRateFinder {
  Set<ExchangeRate> getExchangeRatesForAllDates(String symbol);

  ExchangeRate getExchangeRateForDate(String symbol, String date);
}
