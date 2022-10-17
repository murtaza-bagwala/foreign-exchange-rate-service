package com.crewmeister.cmcodingchallenge.controllers;

import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

  @MockBean
  private CurrencyService currencyService;

  @Autowired
  MockMvc mockMvc;

  private List<Currency> currencies;

  @BeforeEach
  public void setup() {
    Currency currencyINR = new Currency("INR", "INDIA");
    Currency currencyUSD = new Currency("USD", "AMERICA");
    Currency currencyAED = new Currency("AED", "DUBAI");
    currencies = Arrays.asList(currencyINR, currencyUSD, currencyAED);
  }

  @Test
  @DisplayName("Fetches currencies and status ok")
  public void testGetCurrencies() throws Exception {
    Mockito.when(currencyService.fetchCurrencies()).thenReturn(currencies);
    mockMvc.perform(get("/api/currencies"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].symbol").value("INR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].symbol").value("USD"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].symbol").value("AED"));
  }

  @Test
  @DisplayName("Returns empty records when the service return empty")
  public void testGetEmptyCurrencies() throws Exception {
    Mockito.when(currencyService.fetchCurrencies()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/currencies"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));
  }

  @Test
  @DisplayName("Returns 503 if the external service service is not reachable")
  public void testGetException() throws Exception {
    Mockito.when(currencyService.fetchCurrencies()).thenThrow(ResponseStatusException.class);
    mockMvc.perform(get("/api/currencies"))
            .andExpect(status().isServiceUnavailable());
  }
}
