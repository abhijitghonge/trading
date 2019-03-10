package com.ag.trading.data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * TODO: Make it immutable
 */
public final class Delta {
    private LocalDate asOfDate;
    private Double deltaQuantity;
    private BigDecimal deltaAmount;

    private Delta(LocalDate asOfDate, Double deltaQuantity, BigDecimal deltaAmount) {
        this.asOfDate = asOfDate;
        this.deltaQuantity = deltaQuantity;
        this.deltaAmount = deltaAmount;
    }

    public static Delta of(Trade trade) {
        return new Delta(trade.getTradeDate(), trade.getQuantity(), trade.getAmount());
    }

    public LocalDate getAsOfDate() {
        return asOfDate;
    }

    public Double getDeltaQuantity() {
        return deltaQuantity;
    }

    public BigDecimal getDeltaAmount() {
        return deltaAmount;
    }

    public Delta add(Delta previous) {
        return new Delta(this.getAsOfDate(),
                this.deltaQuantity + previous.deltaQuantity,
                this.deltaAmount.add(previous.deltaAmount));
    }
}
