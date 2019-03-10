package com.ag.trading.data;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public enum Trades {
    INSTANCE;
    private Map<String, NavigableMap<LocalDate, List<Trade>>> securityTradesMap;

    Trades(){
        this.securityTradesMap = new ConcurrentHashMap<>();
    }


    public Map<String, NavigableMap<LocalDate, List<Trade>>> getSecurityTradesMap() {
        return securityTradesMap;
    }

    public void register(Trade trade){
        NavigableMap<LocalDate, List<Trade>> localDateTradesMap = securityTradesMap.computeIfAbsent(trade.getSecurity(), k -> new TreeMap<>());
        List<Trade> trades = localDateTradesMap.computeIfAbsent(trade.getTradeDate(), k -> new ArrayList<>());
        trades.add(trade);
    }

}
