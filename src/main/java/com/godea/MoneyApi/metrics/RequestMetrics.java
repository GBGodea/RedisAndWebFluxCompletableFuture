package com.godea.MoneyApi.metrics;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestMetrics {
    private final AtomicInteger usdRequests = new AtomicInteger();
    private final AtomicInteger eurRequests = new AtomicInteger();
    private final AtomicInteger rubRequests = new AtomicInteger();
    private final AtomicInteger errorRequests = new AtomicInteger();

    public void incrementUsdRequests() {
        usdRequests.incrementAndGet();
    }

    public void incrementEurRequests() {
        eurRequests.incrementAndGet();
    }

    public void incrementRubRequests() {
        rubRequests.incrementAndGet();
    }

    public void incrementErrorRequests() {
        errorRequests.incrementAndGet();
    }

    public int getUsdRequests() {
        return usdRequests.get();
    }

    public int getEurRequests() {
        return eurRequests.get();
    }

    public int getRubRequests() {
        return rubRequests.get();
    }

    public int getErrorRequests() {
        return errorRequests.get();
    }
}
