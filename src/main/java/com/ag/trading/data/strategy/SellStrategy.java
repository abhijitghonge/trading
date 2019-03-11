package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

import java.math.BigDecimal;

public class SellStrategy implements TransactionTypeStrategy {
    @Override
    public Position compute(Position previousPosition, Trade trade) {
        if (previousPosition == null) {
            return Position.from(trade);
        }

        Double previousQuantity = previousPosition.getQuantity();
        //Since we can sell only upto previous quantity, break current quantity and hence proportional sale amount
        Double totalSaleQuantity = trade.getQuantity();

        //if selling quantity is less than or equal to previous position quantity
        if (totalSaleQuantity <= previousQuantity) {
            return computeGains(previousPosition, trade, previousQuantity, totalSaleQuantity);

        } else {
            //selling quantity is more than previous quantity, get the negative quantity
            Double negativeQuantity = previousQuantity - totalSaleQuantity ;
            BigDecimal proportionateSalesCost = trade.getAmount().multiply(new BigDecimal(previousQuantity)).divide(new BigDecimal(totalSaleQuantity), BigDecimal.ROUND_CEILING);

            //extract gains
            BigDecimal gainloss = proportionateSalesCost.subtract(previousPosition.getCost());
            //Cost to be associated is preivous cost + gains - total sale amount
            BigDecimal negativeCost = proportionateSalesCost.subtract(trade.getAmount());
            return new Position(trade.getTradeDate(), trade.getSecurity(), negativeQuantity, negativeCost, gainloss);
        }


    }

    private Position computeGains(Position previousPosition, Trade trade, Double previousQuantity, Double totalSaleQuantity) {
        Double remainingQuantity = previousQuantity - totalSaleQuantity;
        BigDecimal proportionateCost = previousPosition.getCost()
                .multiply(new BigDecimal(totalSaleQuantity))
                .divide(new BigDecimal(previousQuantity), BigDecimal.ROUND_CEILING);
        BigDecimal remainingCost = previousPosition.getCost().subtract(proportionateCost);
        BigDecimal gainloss = trade.getAmount().subtract(proportionateCost);

        return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity, remainingCost, gainloss);
    }
}
