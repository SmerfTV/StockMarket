package pl.edu.mimuw.Queues;

import pl.edu.mimuw.Orders.Order;
import java.util.Comparator;
import java.util.Collections;

// Klasa reprezentująca kolejkę zleceń kupna na giełdzie
public class BuyQueue extends Queue {

  // Konstruktor domyślny inicjalizujący pustą kolejkę kupna
  public BuyQueue() {
    super();
  }

  // Nadpisana metoda do sortowania kolejki kupna
  @Override
  public void sortQueue() {
    Collections.sort(this.queue, new Comparator<Order>() {
      @Override
      public int compare(Order o1, Order o2) {
        // Porównanie limitu cenowego zleceń
        // (zlecenia z wyższym limitem mają wyższy priorytet)
        int priceComparison = Integer.compare(o2.getPriceLimit(),
            o1.getPriceLimit());
        if (priceComparison != 0) {
          return priceComparison;
        }

        // Jeśli limity cenowe są równe, porównanie
        // tury dodania (starsze zlecenia mają wyższy priorytet)
        int tourComparison = Integer.compare(o1.getTourAdded(),
            o2.getTourAdded());
        if (tourComparison != 0) {
          return tourComparison;
        }

        // Jeśli tury dodania są równe, porównanie
        // licznika zleceń (starsze zlecenia mają wyższy priorytet)
        return Integer.compare(o1.getCounter(), o2.getCounter());
      }
    });
  }
}
