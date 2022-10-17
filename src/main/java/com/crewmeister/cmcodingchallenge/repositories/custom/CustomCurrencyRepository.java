package com.crewmeister.cmcodingchallenge.repositories.custom;

import com.crewmeister.cmcodingchallenge.models.Currency;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomCurrencyRepository {
    Optional<Currency> findBySymbol(String symbol);
}
