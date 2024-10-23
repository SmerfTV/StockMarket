package pl.edu.mimuw.StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.RandomInvestor;
import pl.edu.mimuw.Orders.ExpirationOrder;
import pl.edu.mimuw.Orders.OrderType;
import java.util.HashMap;

public class ExpirationOrderTest {

  @Test
  public void testExpirationOrderExpiration() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, wallet);
    TurnCounter counter = new TurnCounter(10);

    ExpirationOrder order = new ExpirationOrder(OrderType.BUY, 10, "APL", 100,
        investor, counter, 4);

    assertFalse(order.expiration <= counter.getCurrentTurn());

    for (int i = 0; i < 5; i++) {
      counter.newTurn();
    }

    assertTrue(order.expiration <= counter.getCurrentTurn());
  }
}