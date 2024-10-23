package pl.edu.mimuw.Orders;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;

// Klasa reprezentująca zlecenie bezterminowe na giełdzie
public class IndefiniteOrder extends Order {

  // Konstruktor inicjalizujący zlecenie bezterminowe z podanymi parametrami
  public IndefiniteOrder(OrderType orderType, int shareAmmount,
      String shareName, int priceLimit, Investor investor,
      TurnCounter counter) {
    super(orderType, shareAmmount, shareName, priceLimit, investor, counter);
  }

  // Metoda klonująca zlecenie bezterminowe
  @Override
  public IndefiniteOrder clone() throws CloneNotSupportedException {
    return (IndefiniteOrder) super.clone();
  }

  // Metoda zwracająca tekstową reprezentację zlecenia bezterminowego
  @Override
  public String toString() {
    return String.format(
        "IndefiniteOrder[type=%s, amount=%d, "
            + "name=%s, price=%d, tour=%d, counter=%d] \n",
        orderType, shareAmmount, shareName, priceLimit, tourAdded, counter);
  }
}
