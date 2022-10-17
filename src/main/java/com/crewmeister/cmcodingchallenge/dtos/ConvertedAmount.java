package com.crewmeister.cmcodingchallenge.dtos;

import java.math.BigDecimal;

public class ConvertedAmount {
  private double convertedAmount;

   public ConvertedAmount(double amount, BigDecimal currentPrice) {
    this.convertedAmount = currentPrice.multiply(BigDecimal.valueOf(amount)).doubleValue();
  }

  public double getConvertedAmount() {
    return convertedAmount;
  }
}
