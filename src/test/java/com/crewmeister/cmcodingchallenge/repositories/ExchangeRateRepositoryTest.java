package com.crewmeister.cmcodingchallenge.repositories;

import com.crewmeister.cmcodingchallenge.CmCodingChallengeApplication;
import com.crewmeister.cmcodingchallenge.models.Currency;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = CmCodingChallengeApplication.class)
public class ExchangeRateRepositoryTest {

  @Autowired
  private ExchangeRateRepository exchangeRateRepository;

  @Autowired
  private CurrencyRepository currencyRepository;

  @Test
  public void testFindByCurrencyAndDate() {
    Currency currency = new Currency("INR", "INDIA");
    Currency currencyUSD = new Currency( "USD", "AMERICA");

    currencyRepository.saveAll(Arrays.asList(currency, currencyUSD));

    currency = currencyRepository.findBySymbol("INR").get();
    currencyUSD = currencyRepository.findBySymbol("USD").get();

    ExchangeRate exchangeRate1 = new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(20.0));
    exchangeRate1.setCurrency(currency);
    ExchangeRate exchangeRate2 = new ExchangeRate(LocalDate.parse("2022-10-16"), BigDecimal.valueOf(19.0));
    exchangeRate2.setCurrency(currencyUSD);
    ExchangeRate exchangeRate3 = new ExchangeRate(LocalDate.parse("2022-10-15"), BigDecimal.valueOf(21.0));
    exchangeRate3.setCurrency(currency);

    exchangeRateRepository.saveAll(Arrays.asList(exchangeRate1, exchangeRate2, exchangeRate3));
    Optional<ExchangeRate> result = exchangeRateRepository.findByCurrencyAndDate(currencyUSD, LocalDate.parse("2022-10-16"));
    assertEquals(result.get().getDate(), exchangeRate2.getDate());
    assertEquals(result.get().getValue(), exchangeRate2.getValue());
    assertEquals(result.get().getCurrency(), exchangeRate2.getCurrency());
  }

  @Test
  public void testFindAll() {
    ExchangeRate exchangeRate1 = new ExchangeRate(LocalDate.parse("2022-10-17"), BigDecimal.valueOf(20.0));
    ExchangeRate exchangeRate2 = new ExchangeRate(LocalDate.parse("2022-10-16"), BigDecimal.valueOf(19.0));
    ExchangeRate exchangeRate3 = new ExchangeRate(LocalDate.parse("2022-10-15"), BigDecimal.valueOf(21.0));

    exchangeRateRepository.saveAll(Arrays.asList(exchangeRate1, exchangeRate2, exchangeRate3));
    List<ExchangeRate> result = exchangeRateRepository.findAll();
    assertEquals(result.size(), 3);
  }
}

