package pl.edu.mimuw;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.SMAInvestor;
import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.StockMarket.Simulation;
import pl.edu.mimuw.StockMarket.SharesManager;
import pl.edu.mimuw.Orders.Order;
import pl.edu.mimuw.Orders.OrderType;
import java.util.HashMap;
import java.util.ArrayList;

public class SMAInvestorTest {

  @Test
  public void testCalculateSMA() {
    SMAInvestor investor = new SMAInvestor(1000, new HashMap<>());
    ArrayList<Integer> prices = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      prices.add(i * 10);
    }
    double sma = investor.calculateSMA(prices, 5);
    assertEquals(30.0, sma);
  }

  @Test
  public void testMakeDecisionBuy() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    SMAInvestor investor = new SMAInvestor(1000, wallet);
    List<Investor> testing = new ArrayList<>();
    testing.add(investor);
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(20);
    Simulation simulation = new Simulation(testing, counter, shares);
    SharesManager manager = new SharesManager(counter, 100);
    for (int i = 0; i < 10; i++) {
      manager.setLastTransactionPrice(i + 1);
      manager.updateLastTenTransactions();
    }
    //Last 10 transactions: [1,2,3,4,5,6,7,8,9]
    //Indeks SMA5: 7,5
    //Indeks SMA10: 4,5
    //Investor should buy
    simulation.getShares().put("APL", manager);
    investor.setSimulation(simulation);

    counter.newTurn();
    investor.makeDecision(counter);

    boolean decisionMade = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);

    // Test that decision was made and placed in the appropriate queue
    assertTrue(decisionMade);
  }

  @Test
  public void testMakeDecisionSell() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    SMAInvestor investor = new SMAInvestor(1000, wallet);
    List<Investor> testing = new ArrayList<>();
    testing.add(investor);
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(20);
    Simulation simulation = new Simulation(testing, counter, shares);
    SharesManager manager = new SharesManager(counter, 100);
    for (int i = 0; i < 5; i++) {
      manager.setLastTransactionPrice(10);
      manager.updateLastTenTransactions();
    }
    for (int i = 5; i < 10; i++) {
      manager.setLastTransactionPrice(1);
      manager.updateLastTenTransactions();
    }
    //Last 10 transactions: [1,1,1,1,1,10,10,10,10,10]
    //Indeks SMA5: 1
    //Indeks SMA10: 5,5
    //Investor should sell
    simulation.getShares().put("APL", manager);
    investor.setSimulation(simulation);

    counter.newTurn();
    investor.makeDecision(counter);

    boolean decisionMade = manager.getSell().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);

    // Test that decision was made and placed in the appropriate queue
    assertTrue(decisionMade);
  }

  @Test
  public void testMakeDecisionSellTwo() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    SMAInvestor investor = new SMAInvestor(1000, wallet);
    List<Investor> testing = new ArrayList<>();
    testing.add(investor);
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(20);
    Simulation simulation = new Simulation(testing, counter, shares);
    SharesManager manager = new SharesManager(counter, 100);
    for (int i = 0; i < 5; i++) {
      manager.setLastTransactionPrice(10);
      manager.updateLastTenTransactions();
    }
    for (int i = 5; i < 10; i++) {
      manager.setLastTransactionPrice(1);
      manager.updateLastTenTransactions();
    }

    //PreviousSMA: Sma5 > Sma10
    //AfterSMA: Sma10 > Sma5
    manager.setSma5(15);
    manager.setSma10(10);
    //Last 10 transactions: [1,1,1,1,1,10,10,10,10,10]
    //Indeks SMA5: 1
    //Indeks SMA10: 5,5
    //Investor should sell
    simulation.getShares().put("APL", manager);
    investor.setSimulation(simulation);

    counter.newTurn();
    investor.makeDecision(counter);

    boolean decisionMade = manager.getSell().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);

    // Test that decision was made and placed in the appropriate queue
    assertTrue(decisionMade);
  }

  @Test
  public void testNoDecision() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    SMAInvestor investor = new SMAInvestor(1000, wallet);
    List<Investor> testing = new ArrayList<>();
    testing.add(investor);
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(20);
    Simulation simulation = new Simulation(testing, counter, shares);
    SharesManager manager = new SharesManager(counter, 100);
    for (int i = 0; i < 10; i++) {
      manager.setLastTransactionPrice(10);
      manager.updateLastTenTransactions();
    }
    //Last 10 transactions: [10,10,10,10,10,10,10,10,10,10]
    //Indeks SMA5: 10
    //Indeks SMA10: 10
    //Investor should not buy or sell
    simulation.getShares().put("APL", manager);
    investor.setSimulation(simulation);

    counter.newTurn();
    investor.makeDecision(counter);

    boolean buyDecisionMade = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);
    boolean sellDecisionMade = manager.getSell().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);

    // Test that no decision was made
    assertFalse(buyDecisionMade);
    assertFalse(sellDecisionMade);
  }

  @Test
  public void testNoDecisionDueToInsufficientData() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    SMAInvestor investor = new SMAInvestor(1000, wallet);
    List<Investor> testing = new ArrayList<>();
    testing.add(investor);
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(20);
    Simulation simulation = new Simulation(testing, counter, shares);
    SharesManager manager = new SharesManager(counter, 100);
    for (int i = 0; i < 4; i++) {
      manager.setLastTransactionPrice(10);
      manager.updateLastTenTransactions();
    }
    //Last 4 transactions: [10,10,10,10]
    //Not enough data to calculate SMA5 or SMA10
    simulation.getShares().put("APL", manager);
    investor.setSimulation(simulation);

    counter.newTurn();
    investor.makeDecision(counter);

    boolean buyDecisionMade = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);
    boolean sellDecisionMade = manager.getSell().getQueueArray().stream()
        .anyMatch(order -> order.getInvestor() == investor);

    // Test that no decision was made
    assertFalse(buyDecisionMade);
    assertFalse(sellDecisionMade);
  }
}