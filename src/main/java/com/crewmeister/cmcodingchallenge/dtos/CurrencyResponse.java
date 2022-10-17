package com.crewmeister.cmcodingchallenge.dtos;

public class CurrencyResponse {
  private String symbol;
  private String country;

  public CurrencyResponse(String symbol, String country) {
    this.symbol = symbol;
    this.country = country;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
