package pl.edu.mimuw.Queues;

import java.util.ArrayList;
import pl.edu.mimuw.Orders.Order;

// Abstrakcyjna klasa reprezentująca kolejkę zleceń na giełdzie
public abstract class Queue {

  // Lista przechowująca zlecenia w kolejce
  protected ArrayList<Order> queue;

  // Konstruktor domyślny inicjalizujący pustą kolejkę
  public Queue() {
    this.queue = new ArrayList<>();
  }

  // Konstruktor kopiujący inicjalizujący
  // kolejkę na podstawie innej kolejki
  // (używany do zleceń typu WA)
  public Queue(Queue old) {
    this.queue = new ArrayList<>();
    for (Order order : old.getQueueArray()) {
      try {
        // Klonowanie zleceń z oryginalnej kolejki do nowej kolejki
        Order cloned = order.clone();
        this.queue.add(cloned);
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
  }

  // Metoda dodająca zlecenie do kolejki
  public void add(Order order) {
    queue.add(order);
  }

  // Metoda zwracająca listę zleceń w kolejce
  public ArrayList<Order> getQueueArray() {
    return queue;
  }

  // Abstrakcyjna metoda do sortowania kolejki
  public abstract void sortQueue();
}
