package pl.edu.mimuw.StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.RandomInvestor;
import pl.edu.mimuw.Orders.InstantOrder;
import pl.edu.mimuw.Orders.OrderType;
import java.util.HashMap;

public class InstantOrderTest {

  @Test
  public void testInstantOrderExpiration() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, wallet);
    TurnCounter counter = new TurnCounter(10);

    InstantOrder order = new InstantOrder(OrderType.BUY, 10, "APL", 100,
        investor, counter);

    assertEquals(0, counter.getCurrentTurn());
    assertEquals(0, order.getTourAdded());

    counter.newTurn();

    assertEquals(1, counter.getCurrentTurn());
    assertFalse(order.getTourAdded() == counter.getCurrentTurn());
  }
}