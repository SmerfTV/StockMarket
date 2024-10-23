package pl.edu.mimuw.Investors;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Orders.*;
import pl.edu.mimuw.StockMarket.SharesManager;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RandomInvestor extends Investor {

  // Konstruktor inicjalizujący inwestora z podaną ilością
  // pieniędzy i portfelem akcji
  public RandomInvestor(int money, HashMap<String, Integer> wallet) {
    super(money, wallet);
  }

  // Metoda pomocnicza do losowego wyboru akcji do zakupu z dostępnych akcji
  private String getRandomKeyFromMapForBuy(
      HashMap<String, SharesManager> shares) {
    Random r = new Random();
    List<String> keys = new ArrayList<>(shares.keySet());
    return keys.get(r.nextInt(keys.size()));
  }

  // Metoda pomocnicza do losowego wyboru
  // akcji do sprzedaży z portfela inwestora
  private String getRandomKeyFromMapForSell(HashMap<String, Integer> wallet) {
    Random r = new Random();
    List<String> positiveKeys = new ArrayList<>();
    for (String key : wallet.keySet()) {
      if (wallet.get(key) > 0) {
        positiveKeys.add(key);
      }
    }
    if (positiveKeys.isEmpty()) {
      return null;
    }
    return positiveKeys.get(r.nextInt(positiveKeys.size()));
  }

  // Nadpisana metoda do drukowania portfela inwestora z prefixem "RANDOM"
  @Override
  public void printWallet() {
    System.out.print("RANDOM " + money + " ");
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

  // Nadpisana metoda abstrakcyjna do podejmowania
  // decyzji inwestora na podstawie stanu gry
  @Override
  public void makeDecision(TurnCounter counter) {
    Random generator = new Random();
    OrderType order;
    String randomShare = null;
    Order thisOrder;
    int price;
    int count;

    // Losowanie czy inwestor kupuje czy sprzedaje
    while (true) {
      int buyOrSell = generator.nextInt(2); // Losuje liczbe [0,2)
      if (buyOrSell == 0) {
        // Decyzja o kupnie
        if (this.money == 0) {
          order = OrderType.SELL;
          randomShare = getRandomKeyFromMapForSell(this.wallet);
          if (randomShare == null) {
            return;
          }
          break;
        } else {
          order = OrderType.BUY;
          randomShare = getRandomKeyFromMapForBuy(simulation.getShares());
          break;
        }
      } else {
        // Decyzja o sprzedaży
        order = OrderType.SELL;
        randomShare = getRandomKeyFromMapForSell(this.wallet);
        if (randomShare != null) {
          break;
        } else {
          order = OrderType.BUY;
          randomShare = getRandomKeyFromMapForBuy(simulation.getShares());
          if (this.money == 0) {
            return;
          }
          break;
        }
      }
    }

    // Jeśli nie znaleziono odpowiedniej akcji, zakończ
    if (randomShare == null) {
      return;
    }

    // Określanie ceny i ilości akcji do kupna lub sprzedaży
    if (order == OrderType.SELL) {
      int minBound = Math.max(1,
          simulation.getShareManager(randomShare).getLastTransactionPrice()
              - 10);
      int maxBound =
          simulation.getShareManager(randomShare).getLastTransactionPrice()
              + 10;
      price = generator.nextInt(minBound, maxBound + 1);
      count = generator.nextInt(1, wallet.get(randomShare) + 1);
    } else {
      int minBound = Math.max(1,
          simulation.getShareManager(randomShare).getLastTransactionPrice()
              - 10);
      int maxBound =
          simulation.getShareManager(randomShare).getLastTransactionPrice()
              + 10;
      price = generator.nextInt(minBound, maxBound + 1);
      if (price > money) {
        return;
      }
      count = generator.nextInt(1, (money / price) + 1);
    }

    // Losowanie typu zlecenia
    int orderTypeNumber = generator.nextInt(0, 4);
    if (orderTypeNumber == 0) {
      thisOrder = new DoOrCancelOrder(order, count, randomShare, price, this,
          counter);
    } else if (orderTypeNumber == 1) {
      thisOrder = new IndefiniteOrder(order, count, randomShare, price, this,
          counter);
    } else if (orderTypeNumber == 2) {
      thisOrder = new InstantOrder(order, count, randomShare, price, this,
          counter);
    } else {
      thisOrder = new ExpirationOrder(order, count, randomShare, price, this,
          counter, counter.getCurrentTurn() + generator.nextInt(0, 11));
    }

    // Dodanie zlecenia do odpowiedniego menedżera akcji
    SharesManager thisManager = simulation.getShareManager(randomShare);
    if (order == OrderType.SELL) {
      thisManager.getSell().add(thisOrder);
    } else {
      thisManager.getBuy().add(thisOrder);
    }
  }
}
