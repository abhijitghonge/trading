package com.ag.trading.consume;

import com.ag.trading.data.Delta;
import com.ag.trading.data.Deltas;
import com.ag.trading.data.Trade;

import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class DeltaBuilder implements Consumer<Trade> {

    @Override
    public void accept(Trade trade) {
        Deltas deltas = Deltas.INSTANCE;
        Map<String, NavigableMap<LocalDate, Delta>> securityDeltaMap = deltas.getSecurityDeltaMap();
        securityDeltaMap.compute(trade.getSecurity(),
                (security, localDateDeltaNavigableMap) -> {
                    if (localDateDeltaNavigableMap == null) {
                        localDateDeltaNavigableMap = new TreeMap<>();
                    }
                    localDateDeltaNavigableMap.compute(trade.getTradeDate(),
                            (localDate, delta) -> delta == null ? Delta.of(trade) : delta.add(Delta.of(trade)));
                    return localDateDeltaNavigableMap;

                });
    }
}
