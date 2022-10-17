# foreign-exchange-rate-service
A foreign exchange rate microservice created using Java and SpringBoot.

This application handles currencies and their conversions to euros. 
The API includes fetching the list of supported currencies, their exchange rates and the converted amount.

## Prerequisites
This app uses :-

- Java 11.0.16
- Spring Boot 2.4.1
- In-Memory H2 Database

## Installation

- Clone the repository
- mvn install
- mvn spring-boot:run

After running, the project, switch to your browser and hit 
http://localhost:8080/api/currencies. We should see the list of APIs

Swagger documentation is also added, and it will be up at http://localhost:8080/swagger-ui.html#/

Below is the list of APIs that are implemented :-

- To fetch list of all available currencies

`GET:- http://localhost:8080/api/currencies`

Response:- 

```

[
    {
        "symbol": "AUD",
        "country": "Australia", 
    },
    {
        "symbol": "BGN",
        "country": "Bulgaria",
    },
    {
        "symbol": "BRL",
        "country": "Brazil",
    },
    {
        "symbol": "CAD",
        "country": "Canada",
    }
 ]

```

- To fetch the exchange rates at all available dates for a given currency

`GET:- http://localhost:8080/api/exchangeRates/USD`

Response :- 

```
 [
    {
        "value": 1.3635,
        "date": "2011-02-08"
    },
    {
        "value": 1.102,
        "date": "2016-10-12"
    },
    {
        "value": 0.9975,
        "date": "2002-09-05"
    },
    {
        "value": 0.8587,
        "date": "2000-11-07"
    }
 ]

```
- To fetch the exchange rate at particular date for a given currency

`GET:- http://localhost:8080/api/exchangeRates/USD?date=2000-11-07`

Response :- 

```
[
    {
        "value": 0.86,
        "date": "2000-11-07"
    }
]
```

- To get a foreign exchange amount for a given currency converted to EUR on a particular day

`GET:- http://localhost:8080/api/convert/USD?date=2000-11-07&amount=220`

Response :- 

```
{
    "convertedAmount": 189.2
}
```


