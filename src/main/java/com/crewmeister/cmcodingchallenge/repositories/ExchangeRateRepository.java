package com.crewmeister.cmcodingchallenge.repositories;

import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repositories.custom.CustomExchangeRateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, CustomExchangeRateRepository {
}