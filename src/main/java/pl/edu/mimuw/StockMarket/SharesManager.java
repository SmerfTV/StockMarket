package pl.edu.mimuw.StockMarket;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Queues.BuyQueue;
import pl.edu.mimuw.Queues.SellQueue;
import pl.edu.mimuw.Orders.*;
import java.util.ArrayList;

// Klasa zarządzająca akcją na giełdzie
public class SharesManager {

  // Kolejka zleceń kupna
  private final BuyQueue buy;

  // Kolejka zleceń sprzedaży
  private final SellQueue sell;

  // Licznik tur
  private final TurnCounter counter;

  // Cena ostatniej transakcji
  private int lastTransactionPrice;

  // Lista cen ostatnich dziesięciu transakcji
  private final ArrayList<Integer> lastTenTransactions;

  // Średnia krocząca (SMA) z 5 ostatnich transakcji
  private double sma5;

  // Średnia krocząca (SMA) z 10 ostatnich transakcji
  private double sma10;

  // Konstruktor inicjalizujący SharesManager z licznikiem tur i ceną początkową
  public SharesManager(TurnCounter counter, int priceBeforeStart) {
    this.counter = counter;
    this.lastTenTransactions = new ArrayList<>();
    lastTenTransactions.add(priceBeforeStart);
    this.lastTransactionPrice = priceBeforeStart;
    this.buy = new BuyQueue();
    this.sell = new SellQueue();
    this.sma5 = -1;
    this.sma10 = -1;
  }


  // Setter dla ceny ostatniej transakcji
  public void setLastTransactionPrice(int lastTransactionPrice) {
    this.lastTransactionPrice = lastTransactionPrice;
  }

  // Getter dla SMA5
  public double getSma5() {
    return sma5;
  }

  // Getter dla SMA10
  public double getSma10() {
    return sma10;
  }

  // Setter dla SMA5
  public void setSma5(double sma5) {
    this.sma5 = sma5;
  }

  // Setter dla SMA10
  public void setSma10(double sma10) {
    this.sma10 = sma10;
  }

  // Aktualizacja listy ostatnich dziesięciu transakcji
  public void updateLastTenTransactions() {
    if (lastTenTransactions.size() < 10) {
      lastTenTransactions.addFirst(lastTransactionPrice);
    } else {
      lastTenTransactions.removeLast();
      lastTenTransactions.addFirst(lastTransactionPrice);
    }
  }

  // Getter dla ceny ostatniej transakcji
  public int getLastTransactionPrice() {
    return lastTransactionPrice;
  }

  // Getter dla listy ostatnich dziesięciu transakcji
  public ArrayList<Integer> getLastTenTransactions() {
    return lastTenTransactions;
  }

  // Getter dla kolejki zleceń kupna
  public BuyQueue getBuy() {
    return buy;
  }

  // Getter dla kolejki zleceń sprzedaży
  public SellQueue getSell() {
    return sell;
  }

  // Metoda zwracająca cenę przed dodaniem zlecenia
  public int getBeforeAddedPrice(Order o1, Order o2) {
    if (o1.getTourAdded() != o2.getTourAdded()) {
      return (o1.getTourAdded() < o2.getTourAdded() ? o1.getPriceLimit()
          : o2.getPriceLimit());
    } else {
      return (o1.getCounter() < o2.getCounter() ? o1.getPriceLimit()
          : o2.getPriceLimit());
    }
  }

  //Metoda rekurencyjnie sprwadzająca, czy zlecenie WA może zostać zrealizowane
  private boolean canFullyExecuteOrder(Order order, Queue<Order> tempBuyQueue,
      Queue<Order> tempSellQueue, List<Order> checkedOrders) {
    int totalShares = 0;
    Queue<Order> simulatedBuyQueue = new LinkedList<>(tempBuyQueue);
    Queue<Order> simulatedSellQueue = new LinkedList<>(tempSellQueue);

    if (order.getOrderType() == OrderType.BUY) {
      Investor buyer = order.getInvestor();
      int totalCost = order.getShareAmmount() * order.getPriceLimit();
      if (buyer.getMoney() < totalCost) {
        return false;
      }

      for (Order sellOrder : new LinkedList<>(simulatedSellQueue)) {
        if (sellOrder.getPriceLimit() <= order.getPriceLimit()) {
          if (sellOrder instanceof DoOrCancelOrder && !checkedOrders.contains(
              sellOrder)) {
            checkedOrders.add(sellOrder);
            simulatedSellQueue.remove(sellOrder);
            if (!canFullyExecuteOrder(sellOrder, simulatedBuyQueue,
                simulatedSellQueue, checkedOrders)) {
              tempSellQueue.remove(
                  sellOrder);
              continue;
            }
          }
          totalShares += sellOrder.getShareAmmount();
          simulatedSellQueue.remove(sellOrder);
          if (totalShares >= order.getShareAmmount()) {
            return true;
          }
        }
      }
    } else {
      Investor seller = order.getInvestor();
      if (seller.getWalletCount(order.getShareName())
          < order.getShareAmmount()) {
        return false;
      }

      for (Order buyOrder : new LinkedList<>(simulatedBuyQueue)) {
        if (buyOrder.getPriceLimit() >= order.getPriceLimit()) {
          if (buyOrder instanceof DoOrCancelOrder && !checkedOrders.contains(
              buyOrder)) {
            checkedOrders.add(buyOrder);
            simulatedBuyQueue.remove(buyOrder);
            if (!canFullyExecuteOrder(buyOrder, simulatedBuyQueue,
                simulatedSellQueue, checkedOrders)) {
              tempBuyQueue.remove(
                  buyOrder);
              continue;
            }
          }
          totalShares += buyOrder.getShareAmmount();
          simulatedBuyQueue.remove(buyOrder);
          if (totalShares >= order.getShareAmmount()) {
            return true;
          }
        }
      }
    }

    return totalShares >= order.getShareAmmount();
  }

  // Metoda wykonująca zlecenie kupna i sprzedaży
  private void executeOrder(Order buyOrder, Order sellOrder, int count,
      int price) {
    Investor buyer = buyOrder.getInvestor();
    Investor seller = sellOrder.getInvestor();
    seller.addMoney(count * price);
    buyer.removeMoney(count * price);
    seller.removeStocks(sellOrder.getShareName(), count);
    buyer.addStocks(buyOrder.getShareName(), count);
    buyOrder.setShareAmmount(buyOrder.getShareAmmount() - count);
    sellOrder.setShareAmmount(sellOrder.getShareAmmount() - count);
    this.setLastTransactionPrice(price);
  }

  // Metoda przetwarzająca zlecenia kupna i sprzedaży
  public void processOrders() {
    List<Order> toRemove = new ArrayList<>();

    // Sprawdź zlecenia kupna
    for (Order buyOrder : buy.getQueueArray()) {
      if (buyOrder instanceof DoOrCancelOrder) {
        Queue<Order> tempBuyQueue = new LinkedList<>(buy.getQueueArray());
        Queue<Order> tempSellQueue = new LinkedList<>(sell.getQueueArray());
        tempBuyQueue.remove(buyOrder);
        if (!canFullyExecuteOrder(buyOrder, tempBuyQueue, tempSellQueue,
            new ArrayList<>())) {
          toRemove.add(buyOrder);
        }
      }
    }

    // Sprawdź zlecenia sprzedaży
    for (Order sellOrder : sell.getQueueArray()) {
      if (sellOrder instanceof DoOrCancelOrder) {
        Queue<Order> tempBuyQueue = new LinkedList<>(buy.getQueueArray());
        Queue<Order> tempSellQueue = new LinkedList<>(sell.getQueueArray());
        tempSellQueue.remove(sellOrder);
        if (!canFullyExecuteOrder(sellOrder, tempBuyQueue, tempSellQueue,
            new ArrayList<>())) {
          toRemove.add(sellOrder);
        }
      }
    }

    buy.getQueueArray().removeAll(toRemove);
    sell.getQueueArray().removeAll(toRemove);

    buy.getQueueArray().removeAll(toRemove);
    sell.getQueueArray().removeAll(toRemove);

    while (!buy.getQueueArray().isEmpty() && !sell.getQueueArray().isEmpty()) {
      Order buyOrder = buy.getQueueArray().getFirst();
      Order sellOrder = sell.getQueueArray().getFirst();
      Investor buyer = buyOrder.getInvestor();
      Investor seller = sellOrder.getInvestor();

      if (buyOrder.getPriceLimit() >= sellOrder.getPriceLimit()) {
        int count = Math.min(buyOrder.getShareAmmount(),
            sellOrder.getShareAmmount());
        int price = getBeforeAddedPrice(buyOrder, sellOrder);
        if (seller.getWalletCount(sellOrder.getShareName()) < count) {
          sell.getQueueArray().removeFirst();
          continue;
        }
        if (count * price > buyer.getMoney()) {
          buy.getQueueArray().removeFirst();
          continue;
        }
        executeOrder(buyOrder, sellOrder, count, price);

        if (buyOrder.getShareAmmount() == 0) {
          buy.getQueueArray().removeFirst();
        }
        if (sellOrder.getShareAmmount() == 0) {
          sell.getQueueArray().removeFirst();
        }
      } else {
        break;
      }
    }
  }

  // Metoda czyszcząca zlecenia natychmiastowe i wygasłe zlecenia
  public void cleanOrders() {
    for (int i = 0; i < buy.getQueueArray().size(); i++) {
      Order actualOrder = buy.getQueueArray().get(i);
      if (actualOrder instanceof InstantOrder
          || actualOrder instanceof DoOrCancelOrder) {
        buy.getQueueArray().remove(i);
        i--;
      } else if (actualOrder instanceof ExpirationOrder) {
        if (counter.getCurrentTurn()
            >= ((ExpirationOrder) actualOrder).expiration) {
          buy.getQueueArray().remove(i);
          i--;
        }
      }
    }
    for (int i = 0; i < sell.getQueueArray().size(); i++) {
      Order actualOrder = sell.getQueueArray().get(i);
      if (actualOrder instanceof InstantOrder
          || actualOrder instanceof DoOrCancelOrder) {
        sell.getQueueArray().remove(i);
        i--;
      } else if (actualOrder instanceof ExpirationOrder) {
        if (counter.getCurrentTurn()
            >= ((ExpirationOrder) actualOrder).expiration) {
          sell.getQueueArray().remove(i);
          i--;
        }
      }
    }
  }
}
