package com.ag.trading.consume;

import com.ag.trading.data.Deltas;
import com.ag.trading.data.Positions;
import com.ag.trading.data.Trade;

import java.io.IOException;
import java.util.function.Consumer;

public class MainApplication {
    public static void main(String[] args) throws IOException {
        String filePath = "trades.csv";

        TradeProcessor tradeProcessor = new TradeProcessor(",", filePath);

        Consumer<Trade> tradeConsumer1 = new DeltaBuilder();

        tradeProcessor.process(tradeConsumer1);

        Positions.INSTANCE.applyDeltas(Deltas.INSTANCE);

        System.out.println(Positions.INSTANCE.getSecurityPositionsMap());



    }
}
