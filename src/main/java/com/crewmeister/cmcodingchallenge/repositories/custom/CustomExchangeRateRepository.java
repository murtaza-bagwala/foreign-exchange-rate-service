package com.crewmeister.cmcodingchallenge.repositories.custom;

import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.Optional;

@Repository
public interface CustomExchangeRateRepository {
  Optional<ExchangeRate> findByCurrencyAndDate(Currency currency, LocalDate date);

}
