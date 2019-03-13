package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuyStrategy implements TransactionTypeStrategy {
    @Override
    public Position compute(Position previousPosition, Trade trade) {
        if (previousPosition == null) {
            return Position.from(trade);
        }

        Double previousQuantity = previousPosition.getQuantity();
        //Since we can buy only upto last short position
        Double totalBuyQuantity = trade.getQuantity();

        //if buy quantity is 0 or more than 0, then simply add to previous position
        if (previousQuantity >= 0.0) {
            return addBuy(previousPosition, trade, previousQuantity, totalBuyQuantity);

        } else {
            //get the overflowing buy quantity
            Double remainingQuantity = previousQuantity + totalBuyQuantity;

            if(remainingQuantity <= 0 ){
                BigDecimal proportionalRemainingCost = previousPosition.getCost()
                        .multiply(new BigDecimal(remainingQuantity))
                        .divide(new BigDecimal(previousQuantity),BigDecimal.ROUND_CEILING);
                BigDecimal gainloss = previousPosition.getCost()
                        .subtract(proportionalRemainingCost)
                        .add(trade.getAmount())
                        .multiply(new BigDecimal("-1.00"));
                return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity, proportionalRemainingCost, gainloss);

            }else{
                BigDecimal proportionalCloseOutCost = trade.getAmount()
                        .multiply(new BigDecimal(previousQuantity))
                        .divide(new BigDecimal(totalBuyQuantity), BigDecimal.ROUND_CEILING);
                BigDecimal gainloss = previousPosition.getCost()
                        .subtract(proportionalCloseOutCost)
                        .multiply(BigDecimal.valueOf( -1));
                BigDecimal buyCost = trade.getAmount()
                        .add(proportionalCloseOutCost);
                return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity,buyCost, gainloss );


            }
        }


    }

    private Position addBuy(Position previousPosition, Trade trade, Double previousQuantity, Double totalBuyQuantity) {
        Double newQuantity = previousQuantity + totalBuyQuantity;
        BigDecimal newCost = previousPosition.getCost().add(trade.getAmount());

        return new Position(trade.getTradeDate(), trade.getSecurity(), newQuantity, newCost, BigDecimal.ZERO);
    }
}
