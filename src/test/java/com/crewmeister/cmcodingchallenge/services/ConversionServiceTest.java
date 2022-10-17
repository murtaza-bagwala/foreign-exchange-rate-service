package com.crewmeister.cmcodingchallenge.services;

import com.crewmeister.cmcodingchallenge.CmCodingChallengeApplication;
import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repositories.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repositories.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import com.crewmeister.cmcodingchallenge.services.exchange.ConversionService;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateFinder;
import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CmCodingChallengeApplication.class)
public class ConversionServiceTest {

  @Autowired
  private ConversionService conversionService;

  @MockBean
  private ExchangeRateFinder exchangeRateFinder;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private ExchangeRateRepository exchangeRateRepository;

  @BeforeEach
  public void setup() {
    Mockito.when(exchangeRateFinder.getExchangeRateForDate("USD", "2022-10-17"))
            .thenReturn(new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(82.0)));
  }

  @Test
  @DisplayName("Converts given amount to euros for specific date")
  public void testGetConvertedAmount() throws Exception {
    Currency currency = new Currency("USD", "AMERICA");
    currencyRepository.save(currency);
    ConvertedAmount convertedAmount = conversionService.getConvertedAmount("USD", "2022-10-17", 220);
    assertEquals(convertedAmount.getConvertedAmount(), 18040);
  }

  @Test
  @DisplayName("Converts given amount to euros for specific date if exchanged rate is already saved in DB")
  public void testGetConvertedAmountWithExchangeRateAlreadySaved() throws Exception {
    Currency currency = new Currency("INR", "INDIA");
    currencyRepository.save(currency);
    currency =  currencyRepository.findBySymbol(currency.getSymbol()).get();
    ExchangeRate exchangeRate = new ExchangeRate(LocalDate.parse("2022-10-15"), BigDecimal.valueOf(82));
    exchangeRate.setCurrency(currency);
    exchangeRateRepository.save(exchangeRate);
    ConvertedAmount convertedAmount = conversionService.getConvertedAmount("INR", "2022-10-15", 110);
    assertEquals(convertedAmount.getConvertedAmount(), 9020);
  }

  @Test
  @DisplayName("Returns an error if currency not found")
  public void testGetConvertedAmountWithException() throws Exception {
    assertThrows(HttpClientErrorException.class, () -> conversionService.getConvertedAmount("AED", "2022-10-17", 220));
  }

}
