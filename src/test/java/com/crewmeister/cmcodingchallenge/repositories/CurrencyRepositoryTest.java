package com.crewmeister.cmcodingchallenge.repositories;

import com.crewmeister.cmcodingchallenge.CmCodingChallengeApplication;
import com.crewmeister.cmcodingchallenge.models.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.UniqueConstraint;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = CmCodingChallengeApplication.class)
public class CurrencyRepositoryTest {

  @Autowired
  private CurrencyRepository currencyRepository;

  @Test
  public void testFindBySymbol() {
    Currency currency = new Currency("INR", "INDIA");
    currencyRepository.save(currency);
    Optional<Currency> result = currencyRepository.findBySymbol(currency.getSymbol());
    assertEquals(result.get().getSymbol(), currency.getSymbol());
  }

  @Test
  public void testFindAll() {
    Currency currencyINR = new Currency("INR", "INDIA");
    Currency currencyUSD = new Currency("USD", "AMERICA");
    Currency currencyAED = new Currency("AED", "DUBAI");
    currencyRepository.saveAll(Arrays.asList(currencyINR, currencyUSD, currencyAED));
    List<Currency> result = currencyRepository.findAll();
    assertEquals(result.size(), 3);
  }
}

