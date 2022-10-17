package com.crewmeister.cmcodingchallenge.controllers;

import com.crewmeister.cmcodingchallenge.models.Currency;

import com.crewmeister.cmcodingchallenge.services.currency.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

  @Autowired
  private CurrencyService currencyService;

  @GetMapping("/currencies")
  public ResponseEntity<List<Currency>> getCurrencies() {
    try {
      return new ResponseEntity<List<Currency>>(currencyService.fetchCurrencies(), HttpStatus.OK);
    } catch(Exception exception) {
      throw new ResponseStatusException(
              HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage(), exception);
    }
  }
}
