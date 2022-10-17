package com.crewmeister.cmcodingchallenge.controllers;

import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import com.crewmeister.cmcodingchallenge.services.exchange.ConversionService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConversionControllerTest {

  @MockBean
  private ConversionService conversionService;

  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  public void setup() {

  }

  @Test
  @DisplayName("Converts given amount to euros for specific date")
  public void testGetConvertedAmount() throws Exception {
    ConvertedAmount convertedAmount = new ConvertedAmount(220, BigDecimal.valueOf(82.0));
    Mockito.when(conversionService.getConvertedAmount("INR", "2022-10-17", 220)).thenReturn(convertedAmount);
    mockMvc.perform(get("/api/convert/INR?date=2022-10-17&amount=220"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.convertedAmount").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.convertedAmount").value(18040));
  }

  @Test
  @DisplayName("Returns client error if amount or date is not passed")
  public void testGetConvertedAmountWithMissingDateAndAmountError() throws Exception {
    ConvertedAmount convertedAmount = new ConvertedAmount(220, BigDecimal.valueOf(82.0));
    Mockito.when(conversionService.getConvertedAmount("INR", "2022-10-17", 220)).thenReturn(convertedAmount);
    mockMvc.perform(get("/api/convert/INR"))
            .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("Returns not found if currency details is not found")
  public void testGetConvertedAmountWithException() throws Exception {
    ConvertedAmount convertedAmount = new ConvertedAmount(220, BigDecimal.valueOf(82.0));
    Mockito.when(conversionService.getConvertedAmount("INR", "2022-10-17", 220)).thenThrow(ResponseStatusException.class);
    mockMvc.perform(get("/api/convert/INR?date=2022-10-17&amount=220"))
            .andExpect(status().isNotFound());
  }
}
