package pl.edu.mimuw.StockMarket;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// Klasa reprezentująca symulację giełdy
public class Simulation {

  // Lista inwestorów uczestniczących w symulacji
  private final List<Investor> investors;

  // Licznik tur
  private final TurnCounter counter;

  // Mapa zarządzająca akcjami i ich cenami
  private final HashMap<String, SharesManager> shares;

  // Konstruktor inicjalizujący symulację z listą inwestorów,
  // licznikiem tur i mapą akcji oraz ich cen
  public Simulation(List<Investor> investors, TurnCounter counter,
      HashMap<String, Integer> sharesAndPrice) {
    this.investors = investors;
    this.counter = counter;
    this.shares = new HashMap<>();

    for (String share : sharesAndPrice.keySet()) {
      this.shares.put(share,
          new SharesManager(counter, sharesAndPrice.get(share)));
    }
  }

  // Metoda przypisująca symulację inwestorom
  private void giveInvestorsSimulation() {
    for (Investor investor : investors) {
      investor.setSimulation(this);
    }
  }

  // Getter dla mapy akcji
  public HashMap<String, SharesManager> getShares() {
    return shares;
  }

  // Getter dla menedżera danej akcji
  public SharesManager getShareManager(String share) {
    return shares.get(share);
  }

  // Metoda zbierająca zlecenia od inwestorów
  private void getOrders() {
    Collections.shuffle(investors);
    for (Investor investor : investors) {
      investor.makeDecision(counter);
    }
  }

  // Metoda sortująca zlecenia kupna i sprzedaży
  private void sortOrders() {
    for (String action : shares.keySet()) {
      shares.get(action).getSell().sortQueue();
      shares.get(action).getBuy().sortQueue();
    }
  }

  // Metoda przetwarzająca zlecenia (pakietowa prywatność dla testowania)
  void makeOrders() {
    for (String action : shares.keySet()) {
      shares.get(action).processOrders();
    }
  }

  // Metoda czyszcząca zlecenia i aktualizująca transakcje
  private void cleanOrdersAndUpdateTransactions() {
    for (String action : shares.keySet()) {
      shares.get(action).cleanOrders();
      shares.get(action).updateLastTenTransactions();
    }
  }

  // Metoda wykonująca jedną turę symulacji
  // (pakietowa prywatność dla testowania)
  void doOneTour() {
    getOrders();
    sortOrders();
    makeOrders();
    cleanOrdersAndUpdateTransactions();
    counter.newTurn();
  }

  // Metoda wykonująca całą symulację do momentu jej zakończenia
  public void doSimulation() {
    this.giveInvestorsSimulation();
    while (!counter.isComplete()) {
      doOneTour();
    }
  }

  // Metoda drukująca dane inwestorów
  public void printData() {
    for (Investor investor : investors) {
      investor.printWallet();
    }
  }
}
