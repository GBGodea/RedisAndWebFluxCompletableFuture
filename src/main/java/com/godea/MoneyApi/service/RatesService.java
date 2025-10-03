package com.godea.MoneyApi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godea.MoneyApi.model.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
urls:
    https://api.exchangerate-api.com/v4/latest/USD
 */

@Service
public class RatesService {
    public Map<String, CurrencyRate> allRates() throws ExecutionException, InterruptedException, JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api")
                .build();

        CompletableFuture<CurrencyRate> usdRate = webClient.get()
                .uri("/usd")
                .retrieve()
                .bodyToMono(CurrencyRate.class)
                .toFuture();

        CompletableFuture<CurrencyRate> eurRate = webClient.get()
                .uri("/eur")
                .retrieve()
                .bodyToMono(CurrencyRate.class)
                .toFuture();

        CompletableFuture<CurrencyRate> rubRate = webClient.get()
                .uri("/rub")
                .retrieve()
                .bodyToMono(CurrencyRate.class)
                .toFuture();

        CompletableFuture.allOf(
                usdRate,
                eurRate,
                rubRate
        ).join();

        CurrencyRate currencyRateUsd = usdRate.get();
        CurrencyRate currencyRateEur = eurRate.get();
        CurrencyRate currencyRateRub = rubRate.get();

        return Map.of("usd", currencyRateUsd, "eur", currencyRateEur, "rub", currencyRateRub);
    }

    @Cacheable(value = "ALL_CURRENCY_CACHE")
    public CurrencyRate getCurrentRate(String rate) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.exchangerate-api.com/v4/latest")
                .build();

        String currencyRate = webClient.get()
                .uri("/" + rate)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return readJson(currencyRate);
    }

    private CurrencyRate readJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(json, CurrencyRate.class);
    }
}
