package com.crewmeister.cmcodingchallenge.services.currency;

import com.crewmeister.cmcodingchallenge.models.Currency;

import java.util.List;

public interface CurrencyService {
  List<Currency> fetchCurrencies();
}
