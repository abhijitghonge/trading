package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Positions;
import com.ag.trading.data.Trade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class BuyStrategyTest {
    BuyStrategy buyStrategy = new BuyStrategy();

    @Before
    public void setUp() throws Exception {
        Trade buyTrade = Trade.of(new String[]{
                "03-Oct-17", "Adani Ports and Special Economic Zone", "Buy", "379.6981", "-10", "-1000.00"
        });

        Position position = Position.from(buyTrade);
        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> localDatePositionMap = new TreeMap<>();
        localDatePositionMap.put(position.getAsOfDate(), position);
        securityPositionsMap.put(position.getSecurity(), localDatePositionMap);


    }

    @Test
    public void testGain() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17", "Adani Ports and Special Economic Zone", "Sell", "391.75", "10", "1200.00"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = buyStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017, 12, 11)
                , "Adani Ports and Special Economic Zone"
                , 0.0
                , new BigDecimal("0.00")
                , new BigDecimal("-200.0000"));
        Assert.assertEquals(expectedPosition, position);


    }

    @Test
    public void testBuyLessWithGain() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17", "Adani Ports and Special Economic Zone", "Sell", "391.75", "7", "500"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = buyStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017, 12, 11)
                , "Adani Ports and Special Economic Zone"
                , -3.0
                , new BigDecimal("-300.00")
                , new BigDecimal("200.0000"));
        Assert.assertEquals(expectedPosition, position);


    }

    @Test
    public void testBuyLessWithLoss() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17", "Adani Ports and Special Economic Zone", "Sell", "391.75", "7", "900"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = buyStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017, 12, 11)
                , "Adani Ports and Special Economic Zone"
                , -3.0
                , new BigDecimal("-300.00")
                , new BigDecimal("-200.0000"));
        Assert.assertEquals(expectedPosition, position);

    }

    @Test
    public void testBuyMoreWithLoss() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17", "Adani Ports and Special Economic Zone", "Sell", "391.75", "12", "2400"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = buyStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017, 12, 11)
                , "Adani Ports and Special Economic Zone"
                , 2.0
                , new BigDecimal("400")
                , new BigDecimal("-1000.00"));
        Assert.assertEquals(expectedPosition, position);

    }

}