package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

public class DividendStrategy implements TransactionTypeStrategy {
    @Override
    public Position compute(Position position, Trade trade) {
        //TODO add cash
        return null;
    }
}
