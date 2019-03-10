package com.ag.trading.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Singleton class to access Positions everywhere. Global positions holder
 */
public enum Positions {
    INSTANCE;
    private Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap;

    Positions() {
        securityPositionsMap = new HashMap<>();//Empty Position
    }

    public void applyDeltas(Deltas deltas) {


        Map<String, NavigableMap<LocalDate, Delta>> securityDeltaMap = deltas.getSecurityDeltaMap();
        securityDeltaMap.forEach((security, localDateDeltaNavigableMap) -> {
            Delta runningDelta = null;

            for (Map.Entry<LocalDate, Delta> localDateDeltaEntry : localDateDeltaNavigableMap.entrySet()) {
                runningDelta = (runningDelta == null) ? localDateDeltaEntry.getValue() : runningDelta.add(localDateDeltaEntry.getValue());
                NavigableMap<LocalDate, Position> localDatePositionsMap = securityPositionsMap.computeIfAbsent(security, s -> new TreeMap<>());
                if (localDatePositionsMap.containsKey(localDateDeltaEntry.getKey())) {
                    Position position = localDatePositionsMap.get(localDateDeltaEntry.getKey());
                    position.apply(runningDelta);
                } else {
                    localDatePositionsMap.put(localDateDeltaEntry.getKey(),
                            new Position(localDateDeltaEntry.getKey(), security, runningDelta.getDeltaQuantity(), runningDelta.getDeltaAmount(), BigDecimal.ZERO));
                }
            }

        });


    }

    public Map<String, NavigableMap<LocalDate, Position>> getSecurityPositionsMap() {
        return securityPositionsMap;
    }
}
