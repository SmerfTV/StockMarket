package pl.edu.mimuw.Counters;

public class TurnCounter {

  // Zmienna przechowująca limit tur
  private int turnLimit;

  // Zmienna przechowująca aktualny numer tury
  private int turnNumber;

  // Licznik operacji wykonanych w bieżącej turze
  private int turnCounter;

  // Konstruktor inicjalizujący limit tur i
  // ustawiający początkowe wartości zmiennych
  public TurnCounter(int turnLimit) {
    this.turnLimit = turnLimit;
    this.turnNumber = 0;
    this.turnCounter = 0;
  }

  // Metoda zwracająca aktualny numer tury
  public int getCurrentTurn() {
    return turnNumber;
  }

  // Metoda zwracająca limit tur
  public int getTurnLimit() {
    return turnLimit;
  }

  // Metoda zwracająca wartość licznika operacji i zwiększająca go o 1
  public int getCounter() {
    return turnCounter++;
  }

  // Metoda rozpoczynająca nową turę, resetująca licznik operacji i
  // zwiększająca numer tury o 1
  public int newTurn() {
    turnCounter = 0;
    return ++turnNumber;
  }

  // Metoda ustawiająca nowy limit tur
  public void setTurnLimit(int turnLimit) {
    this.turnLimit = turnLimit;
  }

  // Metoda sprawdzająca, czy osiągnięto limit tur
  public boolean isComplete() {
    return turnNumber >= turnLimit;
  }
}
