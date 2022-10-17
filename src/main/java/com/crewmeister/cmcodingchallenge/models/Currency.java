package com.crewmeister.cmcodingchallenge.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Currency {
  @Id
  @GeneratedValue
  private int id;
  @Column(unique = true)
  private String symbol;
  private String country;
  @OneToMany(cascade = CascadeType.ALL)
  private Set<ExchangeRate> exchangeRates;

  public Currency() {

  }

  public Currency(int id, String symbol, String country) {
    this.id = id;
    this.symbol = symbol;
    this.country = country;
  }

  public Currency(String symbol, String country) {
    this.symbol = symbol;
    this.country = country;
  }

  public Set<ExchangeRate> getExchangeRates() {
    return exchangeRates;
  }

  public void setExchangeRates(Set<ExchangeRate> exchangeRates) {
    this.exchangeRates = exchangeRates;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
