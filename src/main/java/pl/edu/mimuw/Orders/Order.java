package pl.edu.mimuw.Orders;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;

public abstract class Order implements Cloneable {

  // Typ zlecenia (BUY lub SELL)
  protected OrderType orderType;

  // Ilość akcji w zleceniu
  protected int shareAmmount;

  // Nazwa akcji
  protected String shareName;

  // Limit ceny w zleceniu
  protected int priceLimit;

  // Inwestor składający zlecenie
  protected Investor investor;

  // Tura, w której zlecenie zostało dodane
  protected int tourAdded;

  // Licznik zleceń (kolejność dodania w danej turze)
  protected int counter;

  // Konstruktor inicjalizujący zlecenie z podanymi parametrami
  public Order(OrderType orderType, int shareAmmount, String shareName,
      int priceLimit, Investor investor, TurnCounter counter) {
    this.orderType = orderType;
    this.shareAmmount = shareAmmount;
    this.shareName = shareName;
    this.priceLimit = priceLimit;
    this.investor = investor;
    this.counter = counter.getCounter();
    this.tourAdded = counter.getCurrentTurn();
  }

  // Metoda klonująca zlecenie
  @Override
  public Order clone() throws CloneNotSupportedException {
    return (Order) super.clone();
  }

  // Getter dla tury dodania zlecenia
  public int getTourAdded() {
    return tourAdded;
  }

  // Getter dla licznika zleceń
  public int getCounter() {
    return counter;
  }

  // Getter dla limitu ceny
  public int getPriceLimit() {
    return priceLimit;
  }

  // Metoda zwracająca tekstową reprezentację zlecenia
  @Override
  public String toString() {
    return String.format(
        "Order[type=%s, amount=%d, name=%s, price=%d, tour=%d, counter=%d]",
        orderType, shareAmmount, shareName, priceLimit, tourAdded, counter);
  }

  // Getter dla ilości akcji
  public int getShareAmmount() {
    return shareAmmount;
  }

  // Setter dla ilości akcji
  public void setShareAmmount(int shareAmmount) {
    this.shareAmmount = shareAmmount;
  }

  // Getter dla typu zlecenia
  public OrderType getOrderType() {
    return orderType;
  }

  // Getter dla inwestora składającego zlecenie
  public Investor getInvestor() {
    return investor;
  }

  // Getter dla nazwy akcji
  public String getShareName() {
    return shareName;
  }
}
