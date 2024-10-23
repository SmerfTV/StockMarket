package pl.edu.mimuw.Utility;

import pl.edu.mimuw.Counters.TurnCounter;
import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.RandomInvestor;
import pl.edu.mimuw.Investors.SMAInvestor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Klasa odpowiedzialna za odczyt danych z pliku
public class DataReader {

  // Lista inwestorów
  private final List<Investor> investors;

  // Mapa akcji z ich cenami
  private final HashMap<String, Integer> shares;

  // Mapa początkowego portfela
  private final HashMap<String, Integer> initialPortfolio;

  // Początkowa ilość pieniędzy
  private int initialMoney;

  // Licznik tur
  private final TurnCounter counter;

  // Konstruktor domyślny inicjalizujący pola
  public DataReader() {
    this.investors = new ArrayList<>();
    this.initialPortfolio = new HashMap<>();
    this.shares = new HashMap<>();
    this.initialMoney = 0;
    this.counter = new TurnCounter(0);
  }

  // Metoda odczytująca dane z pliku
  public void readData(String file) throws IOException, InvalidDataException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String currentLine;
      boolean hasInvestors = false;
      boolean hasPrices = false;
      boolean hasInitialMoney = false;

      while ((currentLine = br.readLine()) != null) {
        currentLine = currentLine.trim();
        if (currentLine.isEmpty() || currentLine.startsWith("#")) {
          continue;
        }
        parseLine(currentLine);
        if (currentLine.matches("^[RS ]+$")) {
          hasInvestors = true;
        }
        if (currentLine.matches("^[A-Z]+:\\d+.*")) {
          hasPrices = true;
        }
        if (currentLine.matches("\\d+\\s+.*")) {
          hasInitialMoney = true;
        }
      }

      if (!hasInvestors) {
        throw new InvalidDataException("Missing investor data");
      }
      if (!hasPrices) {
        throw new InvalidDataException("Missing stock prices data");
      }
      if (!hasInitialMoney) {
        throw new InvalidDataException(
            "Missing initial money and portfolio data");
      }
    }
    setPortfolios();
  }

  // Metoda analizująca pojedynczą linię z pliku
  private void parseLine(String currentline) throws InvalidDataException {
    currentline = currentline.trim();

    if (currentline.isEmpty()) {
      return;
    }

    if (currentline.matches("^[RS ]+$")) {
      initializeInvestors(currentline);
    } else if (currentline.matches("^[A-Z]+:\\d+.*")) {
      if (currentline.matches(".*\\d+.*")) {
        initializePrices(currentline);
      } else {
        initializePortfolio(currentline);
      }
    } else if (currentline.matches("\\d+\\s+.*")) {
      initializeMoneyAndPortfolio(currentline);
    } else {
      throw new InvalidDataException("Invalid data in line: " + currentline);
    }
  }

  // Metoda inicjalizująca inwestorów
  private void initializeInvestors(String currentline)
      throws InvalidDataException {
    String[] parts = currentline.split("\\s+");
    for (String part : parts) {
      switch (part) {
        case "R":
          investors.add(new RandomInvestor(0, new HashMap<>()));
          break;
        case "S":
          investors.add(new SMAInvestor(0, new HashMap<>()));
          break;
        default:
          throw new InvalidDataException("Invalid investor type: " + part);
      }
    }
  }

  // Metoda inicjalizująca ceny akcji
  private void initializePrices(String currentline)
      throws InvalidDataException {
    String[] parts = currentline.split("\\s+");
    for (String part : parts) {
      String[] stockInfo = part.split(":");
      if (stockInfo.length != 2) {
        throw new InvalidDataException("Invalid stock data: " + part);
      }
      String stockName = stockInfo[0];
      int stockPrice;
      try {
        stockPrice = Integer.parseInt(stockInfo[1]);
      } catch (NumberFormatException e) {
        throw new InvalidDataException("Invalid stock price: " + stockInfo[1]);
      }
      shares.put(stockName, stockPrice);
    }
  }

  // Metoda inicjalizująca portfel akcji
  private void initializePortfolio(String currentline)
      throws InvalidDataException {
    String[] parts = currentline.split("\\s+");
    if (parts.length == 0) {
      throw new InvalidDataException("Portfolio data is missing");
    }
    for (String part : parts) {
      String[] stockInfo = part.split(":");
      if (stockInfo.length != 2) {
        throw new InvalidDataException("Invalid portfolio data: " + part);
      }
      String stockName = stockInfo[0];
      int stockAmount;
      try {
        stockAmount = Integer.parseInt(stockInfo[1]);
      } catch (NumberFormatException e) {
        throw new InvalidDataException("Invalid stock amount: " + stockInfo[1]);
      }
      initialPortfolio.put(stockName, stockAmount);
    }
  }

  // Metoda inicjalizująca początkowe pieniądze i portfel akcji
  private void initializeMoneyAndPortfolio(String currentline)
      throws InvalidDataException {
    String[] parts = currentline.split("\\s+");
    try {
      initialMoney = Integer.parseInt(parts[0]);
      if (parts.length < 2) {
        throw new InvalidDataException("Missing portfolio data");
      }
      StringBuilder portfolioLine = new StringBuilder();
      for (int i = 1; i < parts.length; i++) {
        portfolioLine.append(parts[i]).append(" ");
      }
      String portfolioData = portfolioLine.toString().trim();
      if (portfolioData.isEmpty()) {
        throw new InvalidDataException("Portfolio data is missing");
      }
      String[] portfolioParts = portfolioData.split("\\s+");
      for (String part : portfolioParts) {
        if (!part.contains(":") || part.split(":").length != 2) {
          throw new InvalidDataException("Invalid portfolio data: " + part);
        }
        try {
          Integer.parseInt(part.split(":")[1]);
        } catch (NumberFormatException e) {
          throw new InvalidDataException(
              "Invalid stock amount: " + part.split(":")[1]);
        }
      }
      initializePortfolio(portfolioData);
    } catch (NumberFormatException e) {
      throw new InvalidDataException(
          "Invalid initial money amount: " + parts[0]);
    }
  }

  // Metoda ustawiająca portfele inwestorów
  private void setPortfolios() {
    for (Investor investor : investors) {
      investor.setWallet(initialPortfolio);
      investor.setMoney(initialMoney);
    }
  }

  // Metoda ustawiająca limit tur
  public void setCounter(int numberOfTurns) {
    counter.setTurnLimit(numberOfTurns);
  }

  // Metoda drukująca dane odczytane z pliku
  public void printData() {
    System.out.println("Initial Money: " + initialMoney);
    System.out.println("Initial Portfolio: " + initialPortfolio);
    System.out.println("Shares: " + shares);
    System.out.println("Investors: ");
    for (Investor investor : investors) {
      System.out.println("  - " + investor.getClass().getSimpleName() + ": "
          + investor.getWallet() + investor.getMoney());
    }
    System.out.println("Turns: " + counter.getTurnLimit());
  }

  // Getter dla listy inwestorów
  public List<Investor> getInvestors() {
    return investors;
  }

  // Getter dla mapy akcji
  public HashMap<String, Integer> getShares() {
    return shares;
  }

  // Getter dla licznika tur
  public TurnCounter getCounter() {
    return counter;
  }

  // Getter dla początkowej ilości pieniędzy
  public int getInitialMoney() {
    return initialMoney;
  }

  // Getter dla początkowego portfela
  public HashMap<String, Integer> getInitialPortfolio() {
    return initialPortfolio;
  }
}
