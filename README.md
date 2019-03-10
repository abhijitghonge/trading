# trading
Accounting platform for calculating gain loss on equity trades.
Gain loss is calculated as:
1. if the sell is more than buy, then gain is booked on total quantity that is sold, and proportionate cost is associated for short position.
2. If the sell is less than buy, gain is booked on the proportionate cost of sold quantity.
3. Similarly, if short sale is covered by buy, then buy generates gain to the point of negative (short) quantity. positive quantity of buy are created with proportionate cost.

TODO: add examples
