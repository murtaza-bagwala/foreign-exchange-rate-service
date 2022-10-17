package com.crewmeister.cmcodingchallenge.controllers;


import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import com.crewmeister.cmcodingchallenge.services.exchange.ConversionService;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversion;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeRateControllerTest {

  @MockBean
  private ExchangeRateService exchangeRateService;

  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  public void setup() {

  }

  @Test
  @DisplayName("Fetch and Save exchange rate for a given currency and all dates")
  public void testFetchExchangeRateForAllDates() throws Exception {
    ExchangeRate exchangeRate1 = new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(82.0));
    ExchangeRate exchangeRate2 = new ExchangeRate(LocalDate.parse("2022-10-16"), BigDecimal.valueOf(83.0));
    ExchangeRate exchangeRate3 = new ExchangeRate(LocalDate.parse("2022-10-15"), BigDecimal.valueOf(81.0));
    Mockito.when(exchangeRateService.fetchExchangeRateForAllDate("INR"))
            .thenReturn(Arrays.asList(exchangeRate1, exchangeRate2, exchangeRate3).stream().collect(Collectors.toSet()));
    mockMvc.perform(get("/api/exchangeRates/INR"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].date").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].value").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].date").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].value").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].date").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].value").exists());
  }

  @Test
  @DisplayName("Fetch and Save exchange rate for a given currency and date")
  public void testFetchExchangeRateForSpecificDate() throws Exception {
    ExchangeRate exchangeRate = new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(82.0));
    Mockito.when(exchangeRateService.fetchExchangeRateForDate("INR", "2022-10-17")).thenReturn(exchangeRate);
    mockMvc.perform(get("/api/exchangeRates/INR?date=2022-10-17"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].date").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].value").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].date").value("2022-10-17"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].value").value(82.0));
  }

  @Test
  @DisplayName("Returns not found if currency details is not found")
  public void testFetchExchangeRateForAllDateWithException() throws Exception {
    Mockito.when(exchangeRateService.fetchExchangeRateForAllDate("INR")).thenThrow(ResponseStatusException.class);
    mockMvc.perform(get("/api/exchangeRates/INR"))
            .andExpect(status().isNotFound());
  }
}