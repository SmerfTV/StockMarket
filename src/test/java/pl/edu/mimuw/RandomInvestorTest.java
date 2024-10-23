package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Investors.RandomInvestor;
import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.StockMarket.Simulation;
import pl.edu.mimuw.StockMarket.SharesManager;
import java.util.HashMap;
import java.util.ArrayList;

public class RandomInvestorTest {

  @Test
  public void testMakeDecision() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    RandomInvestor investor = new RandomInvestor(1000, wallet);

    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(new ArrayList<>(), counter, shares);
    simulation.getShares().put("APL", new SharesManager(counter, 100));
    investor.setSimulation(simulation);

    counter.newTurn();
    investor.makeDecision(counter);

    SharesManager manager = simulation.getShareManager("APL");

    boolean decisionMade = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor) ||
        manager.getSell().getQueueArray().stream()
            .anyMatch(order -> order.getInvestor() == investor);

    assertTrue(decisionMade);
  }
}