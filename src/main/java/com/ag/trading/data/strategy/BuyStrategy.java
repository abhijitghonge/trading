package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

import java.math.BigDecimal;

public class BuyStrategy implements TransactionTypeStrategy {
    @Override
    public Position compute(Position previousPosition, Trade trade) {
        if (previousPosition == null) {
            return Position.from(trade);
        }

        Double previousQuantity = previousPosition.getQuantity();
        //Since we can buy only upto last short position
        Double totalBuyQuantity = trade.getQuantity();

        //if selling quantity is less than or equal to previous position quantity
        if (previousQuantity >= 0.0) {
            return addBuy(previousPosition, trade, previousQuantity, totalBuyQuantity);

        } else {
            //get the overflowing buy quantity
            Double negativeQuantity = previousQuantity + totalBuyQuantity;
            BigDecimal proportionateSalesCost = trade.getAmount().multiply(new BigDecimal(previousQuantity)).divide(new BigDecimal(totalBuyQuantity), BigDecimal.ROUND_CEILING);

            //extract gains
            BigDecimal gainloss = proportionateSalesCost.subtract(previousPosition.getCost());
            //Cost to be associated is preivous cost + gains - total sale amount
            BigDecimal negativeCost = proportionateSalesCost.subtract(trade.getAmount());
            return new Position(trade.getTradeDate(), trade.getSecurity(), negativeQuantity, negativeCost, gainloss);
        }


    }

    private Position addBuy(Position previousPosition, Trade trade, Double previousQuantity, Double totalBuyQuantity) {
        Double remainingQuantity = previousQuantity + totalBuyQuantity;
        BigDecimal remainingCost = previousPosition.getCost().add(trade.getAmount());

        return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity, remainingCost, BigDecimal.ZERO);
    }
}
