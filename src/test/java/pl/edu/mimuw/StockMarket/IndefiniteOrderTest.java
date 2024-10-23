package pl.edu.mimuw.StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.RandomInvestor;
import pl.edu.mimuw.Orders.IndefiniteOrder;
import pl.edu.mimuw.Orders.OrderType;

import java.util.HashMap;

public class IndefiniteOrderTest {

  @Test
  public void testIndefiniteOrder() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, wallet);
    TurnCounter counter = new TurnCounter(10);

    IndefiniteOrder order = new IndefiniteOrder(OrderType.BUY, 10, "APL", 100,
        investor, counter);

    assertEquals(0, order.getTourAdded());
    counter.newTurn();
    assertEquals(0, order.getTourAdded());
  }
}
