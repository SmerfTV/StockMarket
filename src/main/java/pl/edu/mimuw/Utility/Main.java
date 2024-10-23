package pl.edu.mimuw.Utility;

import pl.edu.mimuw.StockMarket.Simulation;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Klasa główna uruchamiająca symulację
public class Main {

  private static final Logger logger = Logger.getLogger(Main.class.getName());

  // Metoda główna uruchamiająca program
  public static void main(String[] args) {
    // Sprawdzenie poprawności argumentów wejściowych
    if (args.length != 2) {
      logger.warning(
          "Invalid arguments, correct: <file.txt> <number_of_turns>");
      return;
    }

    // Odczyt ścieżki do pliku wejściowego i liczby tur z argumentów
    String inputFilePath = args[0];
    int numberOfTurns;

    try {
      numberOfTurns = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      logger.warning("Number of turns must be an integer.");
      return;
    }

    // Inicjalizacja obiektu DataReader do odczytu danych
    DataReader dataReader = new DataReader();
    try {
      dataReader.readData(inputFilePath);
    } catch (IOException | InvalidDataException e) {
      logger.log(Level.SEVERE, "Exception occurred while reading data", e);
      return;
    }

    // Ustawienie licznika tur
    dataReader.setCounter(numberOfTurns);

    // Inicjalizacja symulacji na podstawie odczytanych danych
    Simulation simulation = new Simulation(dataReader.getInvestors(),
        dataReader.getCounter(), dataReader.getShares());

    // Uruchomienie symulacji
    simulation.doSimulation();

    // Drukowanie wyników symulacji
    simulation.printData();
  }
}