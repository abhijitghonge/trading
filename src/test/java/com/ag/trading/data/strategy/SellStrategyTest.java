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

public class SellStrategyTest {

    SellStrategy sellStrategy = new SellStrategy();

    @Before
    public void setUp() throws Exception {
        Trade buyTrade = Trade.of(new String[]{
                "03-Oct-17","Adani Ports and Special Economic Zone","Buy","379.6981","27","10251.85"
        });

        Position position = Position.from(buyTrade);
        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> localDatePositionMap = new TreeMap<>();
        localDatePositionMap.put(position.getAsOfDate(), position);
        securityPositionsMap.put(position.getSecurity(), localDatePositionMap);


    }

    @Test
    public void testZeroGainLoss() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17","Adani Ports and Special Economic Zone","Sell","391.75","27","10503.21"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = sellStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017,12,11)
                                                    ,"Adani Ports and Special Economic Zone"
                                                    ,0.0
                                                    , new BigDecimal("0.00")
                                                    , new BigDecimal("251.36"));
        Assert.assertEquals(expectedPosition, position);


    }

    @Test
    public void testSellMoreWithLoss() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17","Adani Ports and Special Economic Zone","Sell","391.75","30","10703.21"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = sellStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017,12,11)
                ,"Adani Ports and Special Economic Zone"
                ,-3.0
                , new BigDecimal("-1070.32")
                , new BigDecimal("-618.96"));
        Assert.assertEquals(expectedPosition, position);


    }

    @Test
    public void testSellLessWithGain() {
        Trade sellTrade = Trade.of(new String[]{
                "11-Dec-17","Adani Ports and Special Economic Zone","Sell","391.75","7","10703.21"
        });

        Map<String, NavigableMap<LocalDate, Position>> securityPositionsMap = Positions.INSTANCE.getSecurityPositionsMap();
        NavigableMap<LocalDate, Position> positionNavigableMap = securityPositionsMap.get(sellTrade.getSecurity());
        Map.Entry<LocalDate, Position> lastPositionEntry = positionNavigableMap.lowerEntry(sellTrade.getTradeDate());
        Position position = sellStrategy.compute(lastPositionEntry.getValue(), sellTrade);
        Position expectedPosition = new Position(LocalDate.of(2017,12,11)
                ,"Adani Ports and Special Economic Zone"
                ,20.0
                , new BigDecimal("7593.96")
                , new BigDecimal("8045.32"));
        Assert.assertEquals(expectedPosition, position);


    }


}