package pl.edu.mimuw.Utility;

// Klasa reprezentująca wyjątek rzucany w przypadku nieprawidłowych danych
public class InvalidDataException extends Exception {

  // Konstruktor inicjalizujący wyjątek z wiadomością
  public InvalidDataException(String message) {
    super(message);
  }
}
