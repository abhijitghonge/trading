package com.ag.trading.data;

import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;

public enum Deltas {
    INSTANCE;

    Deltas() {
        this.securityDeltaMap = new ConcurrentHashMap<>();
    }


    private Map<String, NavigableMap<LocalDate, Delta>> securityDeltaMap;


    public Map<String, NavigableMap<LocalDate, Delta>> getSecurityDeltaMap() {
        return securityDeltaMap;
    }
}
