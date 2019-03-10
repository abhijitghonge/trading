package com.ag.trading.consume;

import com.ag.trading.data.*;
import com.ag.trading.data.strategy.BuyStrategy;
import com.ag.trading.data.strategy.DividendStrategy;
import com.ag.trading.data.strategy.SellStrategy;
import com.ag.trading.data.strategy.TransactionTypeStrategy;
import com.google.common.collect.ImmutableMap;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

public class GainLossCalculator implements Consumer<Trade> {

    private ImmutableMap<String, TransactionTypeStrategy> transactionTypeStrategyMap = ImmutableMap.of(
            "Buy", new BuyStrategy(),
            "Sell", new SellStrategy(),
            "Dividend", new DividendStrategy()
    );

    @Override
    public void accept(Trade trade) {
        //Register current trade to trades map
        Trades.INSTANCE.register(trade);

        //Get localdate to trades map
        Map<String, NavigableMap<LocalDate, List<Trade>>> securityTradesMap = Trades.INSTANCE.getSecurityTradesMap();
        NavigableMap<LocalDate, List<Trade>> localDateTradesMap = securityTradesMap.get(trade.getTradeDate());

        //get All trades from trade date to apply
        NavigableMap<LocalDate, List<Trade>> tradesMapFromTradeDate = new TreeMap<>(localDateTradesMap.tailMap(trade.getTradeDate()));
        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> localDatePositionMap = securityPositionsMap.computeIfAbsent(
                trade.getSecurity(), s -> new TreeMap<>());
        //Get all positions before trade date
        TreeMap<LocalDate, Position> positionMapBeforeTrade = new TreeMap<>(localDatePositionMap.headMap(trade.getTradeDate(), false));
        tradesMapFromTradeDate.forEach((localDate, tradesToApply) -> {
            //get position before localdate
            Map.Entry<LocalDate, Position> previousPositionEntry = positionMapBeforeTrade.lowerEntry(localDate);

            positionMapBeforeTrade.put(localDate, computeNewPosition(previousPositionEntry.getValue(), tradesToApply));

        });
        securityPositionsMap.put(trade.getSecurity(), positionMapBeforeTrade);
    }

    private Position computeNewPosition(Position previousPosition, List<Trade> tradesToApply) {

        Position newPosition = previousPosition;
        for (Trade tradeToApply : tradesToApply) {
            TransactionTypeStrategy transactionTypeStrategy = transactionTypeStrategyMap.get(tradeToApply.getTransactionType());
            newPosition = transactionTypeStrategy.compute(newPosition, tradeToApply);
        }

        return newPosition;
    }
}
