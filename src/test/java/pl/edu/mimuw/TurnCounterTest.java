package pl.edu.mimuw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import pl.edu.mimuw.Counters.TurnCounter;

public class TurnCounterTest {

  @Test
  public void testInitialization() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(0, counter.getCurrentTurn());
    assertEquals(10, counter.getTurnLimit());
    assertEquals(0, counter.getCounter());
  }

  @Test
  public void testGetCurrentTurn() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(0, counter.getCurrentTurn());
    counter.newTurn();
    assertEquals(1, counter.getCurrentTurn());
  }

  @Test
  public void testGetTurnLimit() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(10, counter.getTurnLimit());
  }

  @Test
  public void testGetCounter() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(0, counter.getCounter());
    assertEquals(1, counter.getCounter());
    assertEquals(2, counter.getCounter());
  }

  @Test
  public void testNewTurn() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(0, counter.getCurrentTurn());
    counter.newTurn();
    assertEquals(1, counter.getCurrentTurn());
    assertEquals(0, counter.getCounter());
  }

  @Test
  public void testSetTurnLimit() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(10, counter.getTurnLimit());
    counter.setTurnLimit(20);
    assertEquals(20, counter.getTurnLimit());
  }

  @Test
  public void testIsComplete() {
    TurnCounter counter = new TurnCounter(3);
    assertFalse(counter.isComplete());
    counter.newTurn();
    assertFalse(counter.isComplete());
    counter.newTurn();
    assertFalse(counter.isComplete());
    counter.newTurn();
    assertTrue(counter.isComplete());
  }

  @Test
  public void testCounterResetsOnNewTurn() {
    TurnCounter counter = new TurnCounter(10);
    assertEquals(0, counter.getCounter());
    assertEquals(1, counter.getCounter());
    counter.newTurn();
    assertEquals(0, counter.getCounter());
    assertEquals(1, counter.getCounter());
  }
}
