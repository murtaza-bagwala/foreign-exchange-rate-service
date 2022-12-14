package com.crewmeister.cmcodingchallenge.controllers;

import com.crewmeister.cmcodingchallenge.dtos.ExchangeRateResponse;
import com.crewmeister.cmcodingchallenge.models.ExchangeRate;

import com.crewmeister.cmcodingchallenge.services.exchange.ExchangeRateService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/exchangeRates")
public class ExchangeRateController {

  @Autowired
  private ExchangeRateService exchangeRateService;

  @GetMapping("/{symbol}")
  public ResponseEntity<Set<ExchangeRateResponse>> getExchangeRatesForSymbol(@PathVariable String symbol, @RequestParam
    @ApiParam(name = "date", value = "date", example = "1992-10-07") Optional<String> date) {
    Set<ExchangeRate> exchangeRates = new HashSet<>();

    try {
      if (date.isPresent()) {
        exchangeRates.add(exchangeRateService.fetchExchangeRateForDate(symbol, date.get()));
      } else {
        exchangeRates = exchangeRateService.fetchExchangeRateForAllDate(symbol);
      }
      return new ResponseEntity<Set<ExchangeRateResponse>>(exchangeRates.stream()
              .map((x) -> new ExchangeRateResponse(x.getValue(), x.getDate())).collect(Collectors.toSet()), HttpStatus.OK);
    } catch(Exception exception) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, exception.getMessage(), exception);
    }
  }
}