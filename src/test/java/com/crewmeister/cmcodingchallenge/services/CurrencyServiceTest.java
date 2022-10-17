package com.crewmeister.cmcodingchallenge.services;

import com.crewmeister.cmcodingchallenge.CmCodingChallengeApplication;
import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repositories.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repositories.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyFetcher;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import com.crewmeister.cmcodingchallenge.services.exchange.ConversionService;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateFinder;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CmCodingChallengeApplication.class)
public class CurrencyServiceTest {

  @MockBean
  private CurrencyRepository currencyRepository;

  @MockBean
  private CurrencyFetcher currencyFetcher;

  @Autowired
  private CurrencyService currencyService;

  @Test
  @DisplayName("Fetches all the currencies from  external service and stores in DB")
  public void testGetCurrencies() throws Exception {
    Currency currencyINR = new Currency("INR", "INDIA");
    Currency currencyUSD = new Currency("USD", "AMERICA");
    Currency currencyAED = new Currency("AED", "DUBAI");
    List<Currency> currencies = Arrays.asList(currencyINR, currencyUSD, currencyAED);
    Mockito.when(currencyFetcher.loadCurrencies())
            .thenReturn(currencies);
    List<Currency> result = currencyService.fetchCurrencies();
    assertEquals(result.size(), 3);
  }

  @Test
  @DisplayName("Fetches all the currencies from the DB")
  public void testGetCurrenciesFromDB() throws Exception {
    Currency currencyINR = new Currency("INR", "INDIA");
    Currency currencyUSD = new Currency("USD", "AMERICA");
    Currency currencyAED = new Currency("AED", "DUBAI");
    Currency currencyDRM = new Currency("DRM", "KUWAIT");

    Mockito.when(currencyRepository.findAll())
            .thenReturn(Arrays.asList(currencyAED, currencyINR, currencyUSD, currencyDRM));
    List<Currency> result = currencyService.fetchCurrencies();
    assertEquals(result.size(), 4);
  }

  @Test
  @DisplayName("Throws an error")
  public void testGetCurrenciesWithError() throws Exception {
    Mockito.when(currencyFetcher.loadCurrencies())
            .thenThrow(ResponseStatusException.class);
    assertThrows(ResponseStatusException.class, () -> currencyService.fetchCurrencies());

  }
}
