package com.crewmeister.cmcodingchallenge.services;

import com.crewmeister.cmcodingchallenge.CmCodingChallengeApplication;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repositories.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repositories.ExchangeRateRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CmCodingChallengeApplication.class)
public class ExchangeRateServiceTest {

  @Autowired
  private ExchangeRateService exchangeRateService;

  @MockBean
  private ExchangeRateFinder exchangeRateFinder;

  @MockBean
  private ExchangeRateRepository exchangeRateRepository;

  @MockBean
  private CurrencyRepository currencyRepository;

  @Test
  @DisplayName("Fetch and Save exchange rate for a given currency and all dates")
  public void testFetchExchangeRateForAllDates() throws Exception {
    Mockito.when(currencyRepository.findBySymbol("INR"))
            .thenReturn(Optional.of(new Currency("INR", "INDIA")));
    ExchangeRate exchangeRate1 = new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(82.0));
    ExchangeRate exchangeRate2 = new ExchangeRate(LocalDate.parse("2022-10-16"), BigDecimal.valueOf(83.0));
    ExchangeRate exchangeRate3 = new ExchangeRate(LocalDate.parse("2022-10-15"), BigDecimal.valueOf(81.0));
    Mockito.when(exchangeRateFinder.getExchangeRatesForAllDates("INR"))
            .thenReturn(Arrays.asList(exchangeRate1, exchangeRate2, exchangeRate3).stream().collect(Collectors.toSet()));

    Set<ExchangeRate> result = exchangeRateService.fetchExchangeRateForAllDate("INR");
    assertEquals(result.size(), 3);
  }

  @Test
  @DisplayName("Fetch and Save exchange rate for a given currency and date")
  public void testFetchExchangeRateForSpecificDate() throws Exception {
    Mockito.when(currencyRepository.findBySymbol("USA"))
            .thenReturn(Optional.of(new Currency("USA", "AMERICA")));
    ExchangeRate exchangeRate1 = new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(82.0));
    Mockito.when(exchangeRateFinder.getExchangeRateForDate("USA", "2022-10-17"))
            .thenReturn(exchangeRate1);

    ExchangeRate result = exchangeRateService.fetchExchangeRateForDate("USA", "2022-10-17");
    assertEquals(result.getValue().doubleValue(), 82.0);
    assertEquals(result.getDate(), LocalDate.parse("2022-10-17"));
  }

  @Test
  @DisplayName("Fetch exchange rate for a given currency and date from DB")
  public void testFetchExchangeRateForSpecificDateFromDB() throws Exception {
    Currency currency = new Currency("AED", "DUBAI");
    Mockito.when(currencyRepository.findBySymbol("AED"))
            .thenReturn(Optional.of(currency));
    ExchangeRate exchangeRate1 = new ExchangeRate(LocalDate.parse("2022-10-16"), BigDecimal.valueOf(82.0));
    Mockito.when(exchangeRateRepository.findByCurrencyAndDate(currency, LocalDate.parse("2022-10-16")))
            .thenReturn(Optional.of(exchangeRate1));

    ExchangeRate result = exchangeRateService.fetchExchangeRateForDate("AED", "2022-10-16");
    assertEquals(result.getValue().doubleValue(), 82.0);
    assertEquals(result.getDate(), LocalDate.parse("2022-10-16"));
  }

  @Test
  @DisplayName("Returns not found if currency details is not found")
  public void testFetchExchangeRateForAllDateWithException() throws Exception {
    assertThrows(HttpClientErrorException.class, () -> exchangeRateService.fetchExchangeRateForAllDate("INR"));
  }
}
