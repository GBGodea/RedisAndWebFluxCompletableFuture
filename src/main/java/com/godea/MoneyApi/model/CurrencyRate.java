package com.godea.MoneyApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate {
    private String provider;
    private String date;
    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, BigDecimal> entry : rates.entrySet()) {
            sb.append(entry).append("\n");
        }

        return "Provider: " + provider
                + "\ndate: " + date
                + "\nrates: " + sb;
    }
}
