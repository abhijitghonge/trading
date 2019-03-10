package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

public class BuyStrategy implements TransactionTypeStrategy {
    @Override
    public Position compute(Position position, Trade trade) {
        //TODO Reverse logic of Buy
        return null;
    }
}
