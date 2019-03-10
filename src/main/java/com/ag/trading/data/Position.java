package com.ag.trading.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Position {
    private LocalDate asOfDate;
    private String security;
    private Double quantity;
    private BigDecimal cost;
    private BigDecimal gainLoss;


    public Position(LocalDate asOfDate, String security, Double quantity, BigDecimal cost, BigDecimal gainLoss) {
        this.asOfDate = asOfDate;
        this.security = security;
        this.quantity = quantity;
        this.cost = cost;
        this.gainLoss = gainLoss;
    }

    public static Position from(Trade trade) {
        return new Position(trade.getTradeDate()
                , trade.getSecurity()
                , trade.getQuantity()
                , trade.getAmount()
                , BigDecimal.ZERO
                );
    }

    public LocalDate getAsOfDate() {
        return asOfDate;
    }

    public String getSecurity() {
        return security;
    }

    public Double getQuantity() {
        return quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getGainLoss() {
        return gainLoss;
    }

    public void apply(Delta runningDelta) {
        this.quantity += runningDelta.getDeltaQuantity();
        this.cost = this.cost.add(runningDelta.getDeltaAmount());
    }

    @Override
    public String toString() {
        return "Position{" +
                "asOfDate=" + asOfDate +
                ", security='" + security + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", gainLoss=" + gainLoss +
                '}';
    }
}
