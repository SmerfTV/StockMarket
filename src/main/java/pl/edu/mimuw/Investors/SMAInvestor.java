package pl.edu.mimuw.Investors;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Orders.*;
import pl.edu.mimuw.StockMarket.SharesManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SMAInvestor extends Investor {

  // Konstruktor inicjalizujący inwestora z podaną ilością
  // pieniędzy i portfelem akcji
  public SMAInvestor(int money, HashMap<String, Integer> wallet) {
    super(money, wallet);
  }

  // Metoda obliczająca średnią kroczącą (SMA) dla
  // podanego okresu na podstawie cen
  public double calculateSMA(ArrayList<Integer> prices, int period) {
    if (prices.size() < period) {
      return -1;
    }
    int sum = 0;
    for (int i = 0; i < period; i++) {
      sum += prices.get(i);
    }
    return (double) sum / period;
  }

  // Nadpisana metoda do drukowania portfela inwestora z prefixem "SMA"
  @Override
  public void printWallet() {
    System.out.print("SMA " + money + " ");
    int i = 1;
    for (String name : wallet.keySet()) {
      if (i != wallet.size()) {
        System.out.print(name + ":" + wallet.get(name) + " ");
      } else {
        System.out.print(name + ":" + wallet.get(name) + "\n");
      }
      i++;
    }
  }

  // Nadpisana metoda abstrakcyjna do podejmowania decyzji
  // inwestora na podstawie stanu gry
  @Override
  public void makeDecision(TurnCounter counter) {
    ArrayList<Order> possible = new ArrayList<>();
    Random generator = new Random();

    // Iteracja przez wszystkie dostępne akcje
    for (String stock : simulation.getShares().keySet()) {
      SharesManager manager = simulation.getShareManager(stock);
      ArrayList<Integer> lastTenTransactions = manager.getLastTenTransactions();

      double previousSma5 = manager.getSma5();
      double previousSma10 = manager.getSma10();
      double sma5 = calculateSMA(lastTenTransactions, 5);
      double sma10 = calculateSMA(lastTenTransactions, 10);
      manager.setSma5(sma5);
      manager.setSma10(sma10);
      if (sma5 == -1 || sma10 == -1) {
        continue;
      }

      // Sygnał kupna, gdy SMA5 przekracza SMA10
      if (sma5 > sma10 && previousSma5 <= previousSma10) {
        int minBound = Math.max(1,
            simulation.getShareManager(stock).getLastTransactionPrice() - 10);
        int maxBound =
            simulation.getShareManager(stock).getLastTransactionPrice() + 10;
        int price = generator.nextInt(minBound, maxBound + 1);
        if (price > money) {
          continue;
        }
        int count = generator.nextInt(1, (money / price) + 1);
        int orderTypeNumber = generator.nextInt(0, 4);
        Order newOrder;
        if (orderTypeNumber == 0) {
          newOrder = new DoOrCancelOrder(OrderType.BUY, count, stock, price,
              this, counter);
        } else if (orderTypeNumber == 1) {
          newOrder = new IndefiniteOrder(OrderType.BUY, count, stock, price,
              this, counter);
        } else if (orderTypeNumber == 2) {
          newOrder = new InstantOrder(OrderType.BUY, count, stock, price, this,
              counter);
        } else {
          newOrder = new ExpirationOrder(OrderType.BUY, count, stock, price,
              this, counter,
              counter.getCurrentTurn() + generator.nextInt(0, 11));
        }
        possible.add(newOrder);

        // Sygnał sprzedaży, gdy SMA5 spada poniżej SMA10
      } else if (sma5 < sma10 && previousSma5 >= previousSma10
          && getWalletCount(stock) > 0) {
        int minBound = Math.max(1,
            simulation.getShareManager(stock).getLastTransactionPrice() - 10);
        int maxBound =
            simulation.getShareManager(stock).getLastTransactionPrice() + 10;
        int price = generator.nextInt(minBound, maxBound + 1);
        int count = generator.nextInt(1, wallet.get(stock) + 1);
        int orderTypeNumber = generator.nextInt(1, 4);
        Order newOrder;
        if (orderTypeNumber == 0) {
          newOrder = new DoOrCancelOrder(OrderType.SELL, count, stock, price,
              this, counter);
        } else if (orderTypeNumber == 1) {
          newOrder = new IndefiniteOrder(OrderType.SELL, count, stock, price,
              this, counter);
        } else if (orderTypeNumber == 2) {
          newOrder = new InstantOrder(OrderType.SELL, count, stock, price, this,
              counter);
        } else {
          newOrder = new ExpirationOrder(OrderType.SELL, count, stock, price,
              this, counter,
              counter.getCurrentTurn() + generator.nextInt(0, 11));
        }
        possible.add(newOrder);
      }
    }

    // Wybór i dodanie losowego zlecenia z możliwych
    if (!possible.isEmpty()) {
      Order chosen = possible.get(generator.nextInt(possible.size()));
      SharesManager thisManager = simulation.getShareManager(
          chosen.getShareName());
      if (chosen.getOrderType() == OrderType.SELL) {
        thisManager.getSell().add(chosen);
      } else {
        thisManager.getBuy().add(chosen);
      }
    }
  }
}
