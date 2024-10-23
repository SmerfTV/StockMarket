package pl.edu.mimuw.Orders;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;

// Klasa reprezentująca zlecenie z datą wygaśnięcia na giełdzie
public class ExpirationOrder extends Order {

  // Tura, w której zlecenie wygasa
  public int expiration;

  // Konstruktor inicjalizujący zlecenie z datą wygaśnięcia
  // z podanymi parametrami
  public ExpirationOrder(OrderType orderType, int shareAmmount,
      String shareName, int priceLimit, Investor investor, TurnCounter counter,
      int expiration) {
    super(orderType, shareAmmount, shareName, priceLimit, investor, counter);
    this.expiration = expiration;
  }

  // Metoda klonująca zlecenie z datą wygaśnięcia
  @Override
  public ExpirationOrder clone() throws CloneNotSupportedException {
    return (ExpirationOrder) super.clone();
  }

  // Metoda zwracająca tekstową reprezentację zlecenia z datą wygaśnięcia
  @Override
  public String toString() {
    return String.format(
        "ExpirationOrder[type=%s, amount=%d, name=%s, price=%d, "
            + "tour=%d, counter=%d, expiration=%d] \n",
        orderType, shareAmmount, shareName, priceLimit, tourAdded, counter,
        expiration);
  }
}
