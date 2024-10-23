package pl.edu.mimuw.Investors;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.StockMarket.Simulation;
import java.util.HashMap;

public abstract class Investor {

  // Zmienna przechowująca obiekt symulacji giełdy
  protected Simulation simulation;

  // Zmienna przechowująca ilość dostępnych pieniędzy inwestora
  protected int money;

  // Mapa przechowująca portfel inwestora (nazwy akcji i ich ilość)
  protected HashMap<String, Integer> wallet;

  // Konstruktor inicjalizujący inwestora z podaną ilością
  // pieniędzy i portfelem akcji
  public Investor(int money, HashMap<String, Integer> wallet) {
    this.money = money;
    this.wallet = new HashMap<>();
    this.wallet.putAll(wallet);
    this.simulation = null;
  }

  // Metoda zwracająca portfel inwestora
  public HashMap<String, Integer> getWallet() {
    return wallet;
  }

  // Metoda ustawiająca ilość pieniędzy inwestora
  public void setMoney(int money) {
    this.money = money;
  }

  // Metoda zwracająca ilość pieniędzy inwestora
  public int getMoney() {
    return this.money;
  }

  // Metoda ustawiająca portfel inwestora
  public void setWallet(HashMap<String, Integer> wallet) {
    this.wallet = new HashMap<>(wallet);
  }

  // Metoda ustawiająca symulację giełdy dla inwestora
  public void setSimulation(Simulation simulation) {
    this.simulation = simulation;
  }

  // Abstrakcyjna metoda, którą konkretne klasy inwestorów
  // muszą zaimplementować, aby podjąć decyzję w danej turze
  public abstract void makeDecision(TurnCounter counter);

  // Metoda zwracająca ilość akcji danego typu w portfelu inwestora
  public int getWalletCount(String name) {
    return wallet.get(name);
  }

  // Metoda dodająca pieniądze do portfela inwestora
  public void addMoney(int count) {
    money += count;
  }

  // Metoda odejmująca pieniądze z portfela inwestora
  public void removeMoney(int count) {
    money -= count;
  }

  // Metoda dodająca akcje do portfela inwestora
  public void addStocks(String name, int count) {
    int current = wallet.get(name);
    wallet.put(name, current + count);
  }

  // Metoda odejmująca akcje z portfela inwestora
  public void removeStocks(String name, int count) {
    int current = wallet.get(name);
    wallet.put(name, current - count);
  }

  // Metoda drukująca zawartość portfela inwestora (ilość pieniędzy i akcji)
  public void printWallet() {
    System.out.print(money + " ");
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
}
