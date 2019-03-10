package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

public interface TransactionTypeStrategy {

    Position compute(Position position, Trade trade);
}
