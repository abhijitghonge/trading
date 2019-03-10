package com.ag.trading.data;

import com.ag.trading.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Trade {

    private LocalDate tradeDate ;
    private String security;
    private String transactionType;
    private Double price;
    private Double quantity;
    private BigDecimal amount;
    private Trade(){}

    public static Trade of(String[] values){
        Trade trade = new Trade();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
        trade.tradeDate = LocalDate.parse(values[0], formatter);
        trade.security = StringUtils.trimToEmpty(values[1]);
        trade.transactionType = StringUtils.trimToEmpty(values[2]);

        trade.price = Utils.parseDouble(values[3]);
        trade.quantity = Utils.parseDouble(values[4]);
        trade.amount = Utils.parseBigDecimal(values[5]);

        return trade;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public String getSecurity() {
        return security;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Double getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
