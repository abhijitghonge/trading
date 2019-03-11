package com.ag.trading.data.strategy;

import com.ag.trading.data.Position;
import com.ag.trading.data.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SellStrategy implements TransactionTypeStrategy {
    @Override
    public Position compute(Position previousPosition, Trade trade) {
        if (previousPosition == null) {
            return new Position(trade.getTradeDate(),
                    trade.getSecurity(),
                    -1.0 * trade.getQuantity(),
                    trade.getAmount().multiply(new BigDecimal(-1.0)),
                    BigDecimal.ZERO);
        }

        Double previousQuantity = previousPosition.getQuantity();
        //Since we can sell only upto previous quantity, break current quantity and hence proportional sale amount
        Double totalSaleQuantity = trade.getQuantity();

        //If previous position is flat or short
        if (previousQuantity <= 0.0) {
            return addSell(previousPosition, trade, previousQuantity, totalSaleQuantity);
        } else {
            //selling quantity is more than previous quantity, get the negative quantity
            Double remainingQuantity = previousQuantity - totalSaleQuantity;
            if (remainingQuantity >= 0) {
                BigDecimal remainingCost = previousPosition.getCost()
                        .multiply(new BigDecimal(remainingQuantity))
                        .divide(new BigDecimal(previousQuantity), RoundingMode.CEILING);
                BigDecimal gainloss = trade.getAmount().subtract(previousPosition.getCost().subtract(remainingCost));
                return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity, remainingCost, gainloss);
            }else {

                BigDecimal proportionateSalesCost = trade.getAmount().multiply(new BigDecimal(previousQuantity)).divide(new BigDecimal(totalSaleQuantity), BigDecimal.ROUND_CEILING);

                //extract gains
                BigDecimal gainloss = proportionateSalesCost.subtract(previousPosition.getCost());
                //Cost to be associated is preivous cost + gains - total sale amount
                BigDecimal negativeCost = proportionateSalesCost.subtract(trade.getAmount());
                return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity, negativeCost, gainloss);
            }
        }

    }

    private Position addSell(Position previousPosition, Trade trade, Double previousQuantity, Double totalSaleQuantity) {
        Double remainingQuantity = previousQuantity - totalSaleQuantity;
        BigDecimal remainingCost = previousPosition.getCost().subtract(trade.getAmount());

        return new Position(trade.getTradeDate(), trade.getSecurity(), remainingQuantity, remainingCost, BigDecimal.ZERO);
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
