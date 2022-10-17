package com.crewmeister.cmcodingchallenge.controllers;

import com.crewmeister.cmcodingchallenge.dtos.ConvertedAmount;
import com.crewmeister.cmcodingchallenge.services.exchange.ConversionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController()
@RequestMapping("/api/convert")
public class ConversionController {

  @Autowired
  private ConversionService conversionService;

  @GetMapping("/{symbol}")
  public ResponseEntity<ConvertedAmount> getExchangeRatesForSymbol(@PathVariable String symbol,
                                                                   @RequestParam String date, @RequestParam double amount) {
    try {
      return new ResponseEntity<ConvertedAmount>(conversionService.getConvertedAmount(symbol, date, amount), HttpStatus.OK);
    } catch(Exception exception) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, exception.getMessage(), exception);
    }
  }
}
