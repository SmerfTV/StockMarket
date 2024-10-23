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

public class SimulationTest {

  @Test
  public void testExpirationOrderExpiration() {
    // Ustawienie portfela inwestora z 10 akcjami APL
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(0,
        wallet); // Ustawienie początkowych pieniędzy na 0
    List<Investor> investors = new ArrayList<>();
    investors.add(investor);

    // Ustawienie akcji i cen
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investor.setSimulation(simulation);

    // Dodanie zlecenia wygasającego
    ExpirationOrder order = new ExpirationOrder(OrderType.BUY, 10, "APL", 100,
        investor, counter, 999);
    manager.getBuy().add(order);

    for (int i = 0; i < 3; i++) {
      simulation.doOneTour();
    }

    // Sprawdzenie, czy zlecenie zostało usunięte z kolejki po wygaśnięciu
    boolean orderExists = manager.getBuy().getQueueArray().contains(order);
    assertFalse(orderExists,
        "ExpirationOrder should have expired and been removed from the queue.");
  }

  @Test
  public void testInstantOrderExpiration() {
    // Ustawienie portfela inwestora z 10 akcjami APL
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(0,
        wallet); // Ustawienie początkowych pieniędzy na 0
    List<Investor> investors = new ArrayList<>();
    investors.add(investor);

    // Ustawienie akcji i cen
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investor.setSimulation(simulation);

    // Dodanie zlecenia natychmiastowego
    InstantOrder order = new InstantOrder(OrderType.BUY, 10, "APL", 100,
        investor, counter);
    manager.getBuy().add(order);

    simulation.doOneTour();

    // Sprawdzenie, czy zlecenie zostało usunięte z kolejki po zakończeniu tury
    boolean orderExists = manager.getBuy().getQueueArray().contains(order);
    assertFalse(orderExists,
        "InstantOrder should have expired and been "
            + "removed from the queue at the end of the turn.");
  }

  @Test
  public void testIndefiniteOrder() {
    // Ustawienie portfela inwestora z 10 akcjami APL
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(0,
        wallet); // Ustawienie początkowych pieniędzy na 0
    List<Investor> investors = new ArrayList<>();
    investors.add(investor);

    // Ustawienie akcji i cen
    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 100);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investor.setSimulation(simulation);

    // Dodanie zlecenia bezterminowego
    IndefiniteOrder order = new IndefiniteOrder(OrderType.SELL, 10, "APL", 100,
        investor, counter);
    manager.getSell().add(order);

    for (int i = 0; i < 5; i++) {
      simulation.doOneTour();
    }

    // Sprawdzenie, czy zlecenie pozostało w kolejce po kilku turach
    boolean orderExists = manager.getSell().getQueueArray().contains(order);
    assertTrue(orderExists,
        "IndefiniteOrder should not expire and should remain in the queue.");
  }

  @Test
  public void testOrderExecution() {
    // Ustawienie portfeli inwestorów
    HashMap<String, Integer> walletBuyer = new HashMap<>();
    walletBuyer.put("APL", 0); // Brak początkowych akcji
    Investor investorBuyer = new RandomInvestor(15000,
        walletBuyer); // Kupujący z wystarczającą ilością pieniędzy

    HashMap<String, Integer> walletSeller1 = new HashMap<>();
    walletSeller1.put("APL", 10); // Sprzedający 1 z 10 akcjami APL

    HashMap<String, Integer> walletSeller2 = new HashMap<>();
    walletSeller2.put("APL", 25); // Sprzedający 2 z 25 akcjami APL

    HashMap<String, Integer> walletSeller3 = new HashMap<>();
    walletSeller3.put("APL", 30); // Sprzedający 3 z 30 akcjami APL

    Investor investorSeller1 = new RandomInvestor(0,
        walletSeller1); // Sprzedający 1
    Investor investorSeller2 = new RandomInvestor(0,
        walletSeller2); // Sprzedający 2
    Investor investorSeller3 = new RandomInvestor(0,
        walletSeller3); // Sprzedający 3

    List<Investor> investors = new ArrayList<>();
    investors.add(investorBuyer);
    investors.add(investorSeller1);
    investors.add(investorSeller2);
    investors.add(investorSeller3);

    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 120);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investorBuyer.setSimulation(simulation);
    investorSeller1.setSimulation(simulation);
    investorSeller2.setSimulation(simulation);
    investorSeller3.setSimulation(simulation);

    // Dodanie zleceń kupna
    BuyQueue buyQueue = manager.getBuy();
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 100, "APL", 125, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 40, "APL", 122, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 10, "APL", 121, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 30, "APL", 120, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 10, "APL", 119, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 20, "APL", 118, investorBuyer,
            counter));

    // Dodanie zleceń sprzedaży
    SellQueue sellQueue = manager.getSell();
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 10, "APL", 123, investorSeller1,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 25, "APL", 124, investorSeller2,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 30, "APL", 125, investorSeller3,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 20, "APL", 126, investorSeller1,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 60, "APL", 127, investorSeller2,
            counter));

    // Po posortowaniu buy: 20x 118, 10x 119, 30 x 120, 10 x 121, 40 x 122, 100 x 125
    // Po sortowaniu sell: 10 x 123, 25 x 124, 30 x 125
    // Kupujący kupi = 30 + 25 + 10 = 65
    // Dodane najpierw, więc kupi po cenie 125
    simulation.makeOrders();

    // Sprawdzenie wyników po jednej turze
    assertEquals(65, investorBuyer.getWallet()
        .get("APL")); // Kupujący powinien mieć 65 akcji APL
    assertEquals(15000 - 65 * 125,
        investorBuyer.getMoney()); // Prawidłowe odjęcie pieniędzy

    assertEquals(0, investorSeller1.getWallet()
        .get("APL")); // Sprzedający 1 sprzedał 10 akcji po 125
    assertEquals(1250, investorSeller1.getMoney()); // 10 * 125

    assertEquals(0, investorSeller2.getWallet()
        .get("APL")); // Sprzedający 2 sprzedał 25 akcji po 125
    assertEquals(3125, investorSeller2.getMoney()); // 25 * 125

    assertEquals(0, investorSeller3.getWallet()
        .get("APL")); // Sprzedający 3 sprzedał 30 akcji po 125
    assertEquals(3750, investorSeller3.getMoney()); // 30 * 125
  }

  @Test
  public void testOrderExecutionFirstSellOrders() {
    // Ustawienie portfeli inwestorów
    HashMap<String, Integer> walletBuyer = new HashMap<>();
    walletBuyer.put("APL", 0); // Brak początkowych akcji
    Investor investorBuyer = new RandomInvestor(15000,
        walletBuyer); // Kupujący z wystarczającą ilością pieniędzy

    HashMap<String, Integer> walletSeller1 = new HashMap<>();
    walletSeller1.put("APL", 10); // Sprzedający 1 z 10 akcjami APL

    HashMap<String, Integer> walletSeller2 = new HashMap<>();
    walletSeller2.put("APL", 25); // Sprzedający 2 z 25 akcjami APL

    HashMap<String, Integer> walletSeller3 = new HashMap<>();
    walletSeller3.put("APL", 30); // Sprzedający 3 z 30 akcjami APL

    Investor investorSeller1 = new RandomInvestor(0,
        walletSeller1); // Sprzedający 1
    Investor investorSeller2 = new RandomInvestor(0,
        walletSeller2); // Sprzedający 2
    Investor investorSeller3 = new RandomInvestor(0,
        walletSeller3); // Sprzedający 3

    List<Investor> investors = new ArrayList<>();
    investors.add(investorBuyer);
    investors.add(investorSeller1);
    investors.add(investorSeller2);
    investors.add(investorSeller3);

    HashMap<String, Integer> shares = new HashMap<>();
    shares.put("APL", 120);
    TurnCounter counter = new TurnCounter(10);
    Simulation simulation = new Simulation(investors, counter, shares);
    SharesManager manager = simulation.getShareManager("APL");
    investorBuyer.setSimulation(simulation);
    investorSeller1.setSimulation(simulation);
    investorSeller2.setSimulation(simulation);
    investorSeller3.setSimulation(simulation);

    // Dodanie zleceń sprzedaży
    SellQueue sellQueue = manager.getSell();
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 10, "APL", 123, investorSeller1,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 25, "APL", 124, investorSeller2,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 30, "APL", 125, investorSeller3,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 20, "APL", 126, investorSeller1,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 60, "APL", 127, investorSeller2,
            counter));

    // Dodanie zleceń kupna
    BuyQueue buyQueue = manager.getBuy();
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 100, "APL", 125, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 40, "APL", 122, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 10, "APL", 121, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 30, "APL", 120, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 10, "APL", 119, investorBuyer,
            counter));
    buyQueue.add(
        new IndefiniteOrder(OrderType.BUY, 20, "APL", 118, investorBuyer,
            counter));

    // Po posortowaniu buy: 20x 118, 10x 119, 30 x 120, 10 x 121, 40 x 122, 100 x 125
    // Po sortowaniu sell: 10 x 123, 25 x 124, 30 x 125
    // Kupujący kupi = 30 + 25 + 10 = 65
    simulation.makeOrders();

    // Sprawdzenie wyników po jednej turze
    assertEquals(65, investorBuyer.getWallet()
        .get("APL")); // Kupujący powinien mieć 65 akcji APL
    assertEquals(15000 - (10 * 123 + 25 * 124 + 30 * 125),
        investorBuyer.getMoney()); // Prawidłowe odjęcie pieniędzy

    assertEquals(0, investorSeller1.getWallet()
        .get("APL")); // Sprzedający 1 sprzedał 10 akcji po 123
    assertEquals(1230, investorSeller1.getMoney()); // 10 * 123

    assertEquals(0, investorSeller2.getWallet()
        .get("APL")); // Sprzedający 2 sprzedał 25 akcji po 124
    assertEquals(3100, investorSeller2.getMoney()); // 25 * 124

    assertEquals(0, investorSeller3.getWallet()
        .get("APL")); // Sprzedający 3 sprzedał 30 akcji po 125
    assertEquals(3750, investorSeller3.getMoney()); // 30 * 125
  }

  @Test
  public void testDoOrCancelOrderExecution() {
    // Ustawianie portfeli inwestorów
    HashMap<String, Integer> walletBuyer = new HashMap<>();
    walletBuyer.put("APL", 0); // Brak początkowych akcji
    Investor investorBuyer = new RandomInvestor(15000,
        walletBuyer); // Kupujący z wystarczającą ilością pieniędzy

    HashMap<String, Integer> walletSeller1 = new HashMap<>();
    walletSeller1.put("APL", 10); // Sprzedający 1 z 10 akcjami APL

    HashMap<String, Integer> walletSeller2 = new HashMap<>();
    walletSeller2.put("APL", 25); // Sprzedający 2 z 25 akcjami APL

    HashMap<String, Integer> walletSeller3 = new HashMap<>();
    walletSeller3.put("APL", 30); // Sprzedający 3 z 30 akcjami APL

    Investor investorSeller1 = new RandomInvestor(0,
        walletSeller1); // Sprzedający 1
    Investor investorSeller2 = new RandomInvestor(0,
        walletSeller2); // Sprzedający 2
    Investor investorSeller3 = new RandomInvestor(0,
        walletSeller3); // Sprzedający 3

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

    // Dodanie zlecenia WA kupna, które nie może zostać w pełni zrealizowane
    BuyQueue buyQueue = manager.getBuy();
    buyQueue.add(
        new DoOrCancelOrder(OrderType.BUY, 100, "APL", 125, investorBuyer,
            counter));

    // Dodanie zleceń sprzedaży
    SellQueue sellQueue = manager.getSell();
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 10, "APL", 123, investorSeller1,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 25, "APL", 124, investorSeller2,
            counter));
    sellQueue.add(
        new IndefiniteOrder(OrderType.SELL, 30, "APL", 125, investorSeller3,
            counter));

    simulation.makeOrders();

    // Sprawdzanie, czy zlecenie WA
    // zostało usunięte, ponieważ nie mogło zostać w pełni zrealizowane
    boolean orderExists = manager.getBuy().getQueueArray().stream()
        .anyMatch(order -> order instanceof DoOrCancelOrder);
    assertFalse(orderExists,
        "WA order should be terminated");
  }

}
