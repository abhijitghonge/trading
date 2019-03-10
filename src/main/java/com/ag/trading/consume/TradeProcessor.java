package com.ag.trading.consume;

import com.ag.trading.data.Trade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class TradeProcessor {

    private final String delimiter;

    private final String filePath;

    public TradeProcessor(String delimiter, String filePath) {
        this.delimiter = delimiter;
        this.filePath = filePath;
    }

    public void process(Consumer<Trade> tradeConsumer) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filePath)))) {
            reader.lines().skip(1).map(this::mapTo).forEach(tradeConsumer::accept);
        }
    }

    public Trade mapTo(String record) {
        String[] values = record.split(this.delimiter);
        return Trade.of(values);

    }


}
