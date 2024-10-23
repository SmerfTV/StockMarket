package pl.edu.mimuw.Orders;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;

// Klasa reprezentująca zlecenie typu WA na giełdzie
public class DoOrCancelOrder extends Order {

  // Konstruktor inicjalizujący zlecenie typu WA z podanymi parametrami
  public DoOrCancelOrder(OrderType orderType, int shareAmmount,
      String shareName, int priceLimit, Investor investor,
      TurnCounter counter) {
    super(orderType, shareAmmount, shareName, priceLimit, investor, counter);
  }

  // Metoda klonująca zlecenie typu WA
  @Override
  public DoOrCancelOrder clone() throws CloneNotSupportedException {
    return (DoOrCancelOrder) super.clone();
  }

  // Metoda zwracająca tekstową reprezentację zlecenia typu WA
  @Override
  public String toString() {
    return String.format(
        "DoOrCancelOrder[type=%s, amount=%d, name=%s, price=%d, "
            + "tour=%d, counter=%d] \n",
        orderType, shareAmmount, shareName, priceLimit, tourAdded, counter);
  }
}
