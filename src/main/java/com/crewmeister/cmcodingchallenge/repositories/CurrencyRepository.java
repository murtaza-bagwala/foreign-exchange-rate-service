package com.crewmeister.cmcodingchallenge.repositories;


import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.repositories.custom.CustomCurrencyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>, CustomCurrencyRepository {
}
