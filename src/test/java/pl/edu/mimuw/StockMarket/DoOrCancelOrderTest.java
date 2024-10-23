package pl.edu.mimuw.StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.RandomInvestor;
import pl.edu.mimuw.Orders.*;
import pl.edu.mimuw.Queues.BuyQueue;
import pl.edu.mimuw.Queues.SellQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoOrCancelOrderTest {

  @Test
  public void testDoOrCancelOrderExecuted() {
    // Ustawianie portfeli inwestorów
    HashMap<String, Integer> walletBuyer = new HashMap<>();
    walletBuyer.put("APL", 0); // Brak początkowych akcji
    Investor investorBuyer = new RandomInvestor(15000,
        walletBuyer); // Kupujący z wystarczającą ilością pieniędzy

    HashMap<String, Integer> walletSeller = new HashMap<>();
    walletSeller.put("APL", 100); // Sprzedający z 100 akcjami APL

    Investor investorSeller = new RandomInvestor(0,
        walletSeller); // Sprzedający
    List<Investor> investors = new ArrayList<>();
    investors.add(investorBuyer);
    investors.add(investorSeller);

    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 1000);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investorBuyer.setSimulation(simulation);
    investorSeller.setSimulation(simulation);

    // Dodanie zlecenia WA, które może zostać w pełni zrealizowane
    BuyQueue buyQueue = manager.getBuy();
    buyQueue.add(
        new DoOrCancelOrder(OrderType.BUY, 50, "APL", 120, investorBuyer,
            counter));

    // Dodanie zleceń sprzedaży
    SellQueue sellQueue = manager.getSell();
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 50, "APL", 120, investorSeller,
            counter));

    simulation.makeOrders();

    // Sprawdzanie, czy zlecenie WA zostało zrealizowane
    boolean orderExists = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order instanceof DoOrCancelOrder);
    assertFalse(orderExists,
        "WA order should be realised.");
  }

  @Test
  public void testDoOrCancelOrderNotExecuted() {
    // Ustawianie portfeli inwestorów
    HashMap<String, Integer> walletBuyer = new HashMap<>();
    walletBuyer.put("APL", 0); // Brak początkowych akcji
    Investor investorBuyer = new RandomInvestor(15000,
        walletBuyer); // Kupujący z wystarczającą ilością pieniędzy

    HashMap<String, Integer> walletSeller1 = new HashMap<>();
    walletSeller1.put("APL", 30); // Sprzedający 1 z 30 akcjami APL

    HashMap<String, Integer> walletSeller2 = new HashMap<>();
    walletSeller2.put("APL", 10); // Sprzedający 2 z 10 akcjami APL

    Investor investorSeller1 = new RandomInvestor(0,
        walletSeller1); // Sprzedający 1
    Investor investorSeller2 = new RandomInvestor(0,
        walletSeller2); // Sprzedający 2

    List<Investor> investors = new ArrayList<>();
    investors.add(investorBuyer);
    investors.add(investorSeller1);
    investors.add(investorSeller2);

    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 1000);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investorBuyer.setSimulation(simulation);
    investorSeller1.setSimulation(simulation);
    investorSeller2.setSimulation(simulation);

    // Dodanie zlecenia WA, które nie może zostać w pełni zrealizowane
    BuyQueue buyQueue = manager.getBuy();
    buyQueue.add(
        new DoOrCancelOrder(OrderType.BUY, 50, "APL", 120, investorBuyer,
            counter));

    // Dodanie zleceń sprzedaży
    SellQueue sellQueue = manager.getSell();
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 30, "APL", 120, investorSeller1,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 10, "APL", 120, investorSeller2,
            counter));

    simulation.makeOrders();

    // Sprawdzanie, czy zlecenie WA nie zostało zrealizowane
    boolean orderExists = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order instanceof DoOrCancelOrder);
    assertFalse(orderExists,
        "WA order should be cancaled");
  }

  @Test
  public void testComplexDoOrCancelOrderExecution() {
    HashMap<String, Integer> walletBuyer = new HashMap<>();
    walletBuyer.put("APL", 0); // Brak początkowych akcji
    Investor investorBuyer = new RandomInvestor(15000, walletBuyer); // Kupujący z wystarczającą ilością pieniędzy

    HashMap<String, Integer> walletSeller1 = new HashMap<>();
    walletSeller1.put("APL", 10); // Sprzedający 1 z 10 akcjami APL

    HashMap<String, Integer> walletSeller2 = new HashMap<>();
    walletSeller2.put("APL", 25); // Sprzedający 2 z 25 akcjami APL

    HashMap<String, Integer> walletSeller3 = new HashMap<>();
    walletSeller3.put("APL", 30); // Sprzedający 3 z 30 akcjami APL

    Investor investorSeller1 = new RandomInvestor(0, walletSeller1); // Sprzedający 1
    Investor investorSeller2 = new RandomInvestor(0, walletSeller2); // Sprzedający 2
    Investor investorSeller3 = new RandomInvestor(0, walletSeller3); // Sprzedający 3

    List<Investor> investors = new ArrayList<>();
    investors.add(investorBuyer);
    investors.add(investorSeller1);
    investors.add(investorSeller2);
    investors.add(investorSeller3);

    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 1000);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investorBuyer.setSimulation(simulation);
    investorSeller1.setSimulation(simulation);
    investorSeller2.setSimulation(simulation);
    investorSeller3.setSimulation(simulation);

    // Dodanie zlecenia DoC kupna, które nie może być w pełni zrealizowane
    BuyQueue buyQueue = manager.getBuy();
    buyQueue.add(new DoOrCancelOrder(OrderType.BUY, 100, "APL", 125, investorBuyer, counter));

    // Dodanie zleceń sprzedaży
    SellQueue sellQueue = manager.getSell();
    sellQueue.add(new IndefiniteOrder(OrderType.SELL, 10, "APL", 123, investorSeller1, counter));
    sellQueue.add(new IndefiniteOrder(OrderType.SELL, 25, "APL", 124, investorSeller2, counter));
    sellQueue.add(new IndefiniteOrder(OrderType.SELL, 30, "APL", 125, investorSeller3, counter));

    simulation.makeOrders();

    // Sprawdzanie, czy zlecenie DoC zostało usunięte, ponieważ nie mogło być w pełni zrealizowane
    boolean orderExists = manager.getBuy().getQueueArray().stream().anyMatch(order -> order instanceof DoOrCancelOrder);
    assertFalse(orderExists, "Zlecenie DoOrCancel powinno zostać usunięte, ponieważ nie mogło być w pełni zrealizowane.");
  }
}
