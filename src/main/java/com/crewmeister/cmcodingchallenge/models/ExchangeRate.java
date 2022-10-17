package com.crewmeister.cmcodingchallenge.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table (uniqueConstraints =
        { @UniqueConstraint(name = "UniqueDateAndCurrencyId", columnNames = { "date", "currency_id" })})
public class ExchangeRate {
  @Id
  @GeneratedValue
  private int id;
  private LocalDate date;
  private BigDecimal value;
  @ManyToOne
  @JoinColumn(name = "currency_id")
  Currency currency;

  public ExchangeRate() {

  }

  public ExchangeRate(int id, LocalDate date, BigDecimal value) {
    this.id = id;
    this.date = date;
    this.value = value;
  }

  public ExchangeRate(LocalDate date, BigDecimal value) {
    this.date = date;
    this.value = value;
  }

  public ExchangeRate(int id, LocalDate date, BigDecimal value, Currency currency) {
    this.id = id;
    this.date = date;
    this.value = value;
    this.currency = currency;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Currency getCurrency() {
    return currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExchangeRate that = (ExchangeRate) o;
    return date.equals(that.date) && value.equals(that.value) && currency.equals(that.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, value, currency);
  }
}
