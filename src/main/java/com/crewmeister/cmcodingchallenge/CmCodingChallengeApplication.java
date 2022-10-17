package com.crewmeister.cmcodingchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

@SpringBootApplication
@EnableSwagger2
public class CmCodingChallengeApplication {

  public static void main(String[] args) {
    SpringApplication.run(CmCodingChallengeApplication.class, args);
  }

  @Bean
  public Docket currencyAndExchangeAPI() {
    return new Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("com.crewmeister.cmcodingchallenge")).build()
            .genericModelSubstitutes(Optional.class);
  }

}
