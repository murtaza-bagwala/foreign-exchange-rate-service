package com.crewmeister.cmcodingchallenge.services;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class HTMLLoader {
  private static Logger logger = LoggerFactory.getLogger(HTMLLoader.class);

  public static Document getDocument(String url) throws ResponseStatusException {
    Connection connection = Jsoup.connect(url);
    connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    connection.header("Accept-Encoding", "gzip, deflate, sdch");
    connection.header("Accept-Language", "zh-CN,zh;q=0.8");
    connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
    try {
      Document document = connection.get();
      return document;
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
    }
  }
}
