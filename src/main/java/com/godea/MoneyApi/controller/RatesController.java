package com.godea.MoneyApi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.godea.MoneyApi.metrics.RequestMetrics;
import com.godea.MoneyApi.model.CurrencyRate;
import com.godea.MoneyApi.service.RatesService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class RatesController {
    @Autowired
    private RatesService ratesService;
    @Autowired
    private RequestMetrics requestMetrics;

    @GetMapping("/rates")
    public ResponseEntity<?> getAllRates() throws ExecutionException, InterruptedException, JsonProcessingException {
        Map<String, CurrencyRate> currencyRate = ratesService.allRates();

        requestMetrics.incrementUsdRequests();
        requestMetrics.incrementEurRequests();
        requestMetrics.incrementRubRequests();

        return ResponseEntity.ok(Map.of(
                        "usd", currencyRate.get("usd"),
                        "eur", currencyRate.get("eur"),
                        "rub", currencyRate.get("rub"),
                        "timestamp", System.currentTimeMillis()
                )
        );
    }

    @GetMapping("/{rate}")
    public ResponseEntity<?> getCurrentRate(@PathVariable String rate) throws JsonProcessingException {
        switch (rate) {
            case "usd" -> requestMetrics.incrementUsdRequests();
            case "eur" -> requestMetrics.incrementEurRequests();
            case "rub" -> requestMetrics.incrementRubRequests();
            default -> requestMetrics.incrementErrorRequests();
        }

        CurrencyRate currencyRate = ratesService.getCurrentRate(rate);

        return ResponseEntity.ok(currencyRate);
    }

    @GetMapping("/metrics")
    public Map<String, Integer> getMetrices() {
        return Map.of(
                "usdRequests", requestMetrics.getUsdRequests(),
                "eurRequests", requestMetrics.getEurRequests(),
                "rubRequests", requestMetrics.getRubRequests(),
                "errorCount", requestMetrics.getErrorRequests()
        );
    }
}
