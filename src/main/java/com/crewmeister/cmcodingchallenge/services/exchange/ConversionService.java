package com.crewmeister.cmcodingchallenge.services.exchange;

import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;

public interface ConversionService {
  ConvertedAmount getConvertedAmount(String symbol, String date, double amount);
}
