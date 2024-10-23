package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Investors.Investor;
import pl.edu.mimuw.Investors.RandomInvestor;
import java.util.HashMap;

public class InvestorTest {

  @Test
  public void testGetWallet() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, wallet);
    assertEquals(wallet, investor.getWallet());
  }

  @Test
  public void testSetMoney() {
    Investor investor = new RandomInvestor(1000, new HashMap<>());
    investor.setMoney(2000);
    assertEquals(2000, investor.getMoney());
  }

  @Test
  public void testGetMoney() {
    Investor investor = new RandomInvestor(1000, new HashMap<>());
    assertEquals(1000, investor.getMoney());
  }

  @Test
  public void testSetWallet() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, new HashMap<>());
    investor.setWallet(wallet);
    assertEquals(wallet, investor.getWallet());
  }

  @Test
  public void testAddMoney() {
    Investor investor = new RandomInvestor(1000, new HashMap<>());
    investor.addMoney(500);
    assertEquals(1500, investor.getMoney());
  }

  @Test
  public void testRemoveMoney() {
    Investor investor = new RandomInvestor(1000, new HashMap<>());
    investor.removeMoney(500);
    assertEquals(500, investor.getMoney());
  }

  @Test
  public void testAddStocks() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, wallet);
    investor.addStocks("APL", 5);
    assertEquals(15, (int) investor.getWallet().get("APL"));
  }

  @Test
  public void testRemoveStocks() {
    HashMap<String, Integer> wallet = new HashMap<>();
    wallet.put("APL", 10);
    Investor investor = new RandomInvestor(1000, wallet);
    investor.removeStocks("APL", 5);
    assertEquals(5, (int) investor.getWallet().get("APL"));
  }
}
