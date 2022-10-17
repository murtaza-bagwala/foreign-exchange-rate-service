package com.crewmeister.cmcodingchallenge.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRateResponse {
  private BigDecimal value;
  private LocalDate date;

  public ExchangeRateResponse(BigDecimal value, LocalDate date) {
    this.value = value;
    this.date = date;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
}
