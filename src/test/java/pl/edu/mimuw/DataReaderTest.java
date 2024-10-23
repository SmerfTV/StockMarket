package pl.edu.mimuw;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import pl.edu.mimuw.Utility.DataReader;
import pl.edu.mimuw.Utility.InvalidDataException;
import pl.edu.mimuw.Investors.Investor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DataReaderTest {

  private static final String TEMP_FILE = "temp_input.txt";

  private void writeToFile(String content) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE));
    writer.write(content);
    writer.close();
  }

  @Test
  public void testValidDataInput() {
    String input = "# Test data\n" +
        "R R R R S S\n" +
        "APL:145 MSFT:300 GOOGL:2700\n" +
        "100000 APL:5 MSFT:15 GOOGL:3";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      List<Investor> investors = reader.getInvestors();
      HashMap<String, Integer> stockPrices = reader.getShares();
      int initialMoney = reader.getInitialMoney();
      HashMap<String, Integer> initialPortfolio = reader.getInitialPortfolio();

      assertNotNull(investors);
      assertEquals(6, investors.size());
      assertNotNull(stockPrices);
      assertEquals(3, stockPrices.size());
      assertEquals(100000, initialMoney);
      assertNotNull(initialPortfolio);
      assertEquals(3, initialPortfolio.size());

    } catch (InvalidDataException | IOException e) {
      fail("Exception should not be thrown for valid input");
    } finally {
      new File(TEMP_FILE).delete();
    }
  }

  @Test
  public void testInvalidInvestorType() {
    String input = "# Invalid investor type\n" +
        "R R X R S S\n" +
        "APL:145 MSFT:300 GOOGL:2700\n" +
        "100000 APL:5 MSFT:15 GOOGL:3";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      fail("InvalidDataException should be thrown for invalid investor type");
    } catch (InvalidDataException | IOException e) {
      assertTrue(e instanceof InvalidDataException);
    } finally {
      new File(TEMP_FILE).delete();
    }
  }

  @Test
  public void testInvalidStockPrices() {
    String input = "# Invalid stock prices\n" +
        "R R R R S S\n" +
        "APL:145 MSFT:XYZ GOOGL:2700\n" +
        "100000 APL:5 MSFT:15 GOOGL:3";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      fail("InvalidDataException should be thrown for incorrect stock prices");
    } catch (InvalidDataException | IOException e) {
      assertTrue(e instanceof InvalidDataException);
    } finally {
      new File(TEMP_FILE).delete();
    }
  }

  @Test
  public void testInvalidInitialMoney() {
    String input = "# Invalid initial money\n" +
        "R R R R S S\n" +
        "APL:145 MSFT:300 GOOGL:2700\n" +
        "XYZ APL:5 MSFT:15 GOOGL:3";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      fail("InvalidDataException should be thrown for incorrect initial money");
    } catch (InvalidDataException | IOException e) {
      assertTrue(e instanceof InvalidDataException);
    } finally {
      new File(TEMP_FILE).delete();
    }
  }

  @Test
  public void testInvalidPortfolioData() {
    String input = "# Invalid portfolio data\n" +
        "R R R R S S\n" +
        "APL:145 MSFT:300 GOOGL:2700\n" +
        "100000 APL:5 MSFT:15 GOOGL:XYZ";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      fail(
          "InvalidDataException should be thrown for incorrect portfolio data");
    } catch (InvalidDataException | IOException e) {
      assertTrue(e instanceof InvalidDataException);
    } finally {
      new File(TEMP_FILE).delete();
    }
  }

  @Test
  public void testIncompleteData() {
    String input = "# Incomplete data\n" +
        "R R R R S S\n" +
        "APL:145 MSFT:300 GOOGL:2700\n";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      fail("InvalidDataException should be thrown for incomplete data");
    } catch (InvalidDataException | IOException e) {
      assertTrue(e instanceof InvalidDataException);
    } finally {
      new File(TEMP_FILE).delete();
    }
  }

  @Test
  public void testMalformedData() {
    String input = "# Malformed data\n" +
        "R R R R S S\n" +
        "APL:145 MSFT:300 GOOGL:2700\n" +
        "100000 APL3 MSFT:15";

    try {
      writeToFile(input);
      DataReader reader = new DataReader();
      reader.readData(TEMP_FILE);
      fail("InvalidDataException should be thrown for malformed data");
    } catch (InvalidDataException | IOException e) {
      assertTrue(e instanceof InvalidDataException);
    } finally {
      new File(TEMP_FILE).delete();
    }
  }
}
