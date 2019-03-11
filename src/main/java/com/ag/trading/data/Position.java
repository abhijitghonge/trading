package com.ag.trading.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return Objects.equals(asOfDate, position.asOfDate) &&
                Objects.equals(security, position.security) &&
                Objects.equals(quantity, position.quantity) &&
                Objects.equals(cost, position.cost) &&
                Objects.equals(gainLoss, position.gainLoss);
    }

    @Override
    public int hashCode() {

        return Objects.hash(asOfDate, security, quantity, cost, gainLoss);
    }
}
