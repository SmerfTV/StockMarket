package pl.edu.mimuw.Orders;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;

// Klasa reprezentująca natychmiastowe zlecenie na giełdzie
public class InstantOrder extends Order {

  // Konstruktor inicjalizujący natychmiastowe zlecenie z podanymi parametrami
  public InstantOrder(OrderType orderType, int shareAmmount, String shareName,
      int priceLimit, Investor investor, TurnCounter counter) {
    super(orderType, shareAmmount, shareName, priceLimit, investor, counter);
  }

  // Metoda klonująca natychmiastowe zlecenie
  @Override
  public InstantOrder clone() throws CloneNotSupportedException {
    return (InstantOrder) super.clone();
  }

  // Metoda zwracająca tekstową reprezentację natychmiastowego zlecenia
  @Override
  public String toString() {
    return String.format(
        "InstantOrder[type=%s, amount=%d, name=%s, price=%d, "
            + "tour=%d, counter=%d] \n",
        orderType, shareAmmount, shareName, priceLimit, tourAdded, counter);
  }
}
