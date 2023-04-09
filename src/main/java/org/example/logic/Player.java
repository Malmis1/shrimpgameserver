package org.example.logic;

import org.example.ClientHandler;

/**
 * The Player class represents a player in the game. It contains information about the player
 * such as name,
 * <p>
 * money, expenses, island and shrimp pounds caught.
 */
public class Player {
  private final String name;
  private final ClientHandler clientHandler;
  private int money;
  private int expenses;
  private Game game;
  private int shrimpCaught;

  /**
   * Constructor for creating a Player object with the given name, money, expenses, island and
   * shrimp pounds caught.
   *
   * @param name     The name of the player.
   * @param expenses The total expenses of the player.
   */
  public Player(String name, ClientHandler clientHandler, int expenses) {
    this.name = name;
    this.clientHandler = clientHandler;
    this.money = 0;
    this.expenses = expenses;
    this.shrimpCaught = 0;
  }

  /**
   * Returns the name of the player.
   *
   * @return The name of the player.
   */
  public String getName() {
    return this.name;
  }

  public ClientHandler getClientHandler()
  {
    return this.clientHandler;
  }

  /**
   * Returns the amount of money the player has.
   *
   * @return The amount of money the player has.
   */
  public int getMoney() {
    return this.money;
  }

  /**
   * Sets the amount of money the player has.
   *
   * @param money The amount of money the player has.
   */
  public void setMoney(int money) {
    this.money = money;
  }

  /**
   * Returns the island on which the player is currently located.
   *
   * @return The island on which the player is currently located.
   */
  public Game getGame() {
    return this.game;
  }

  /**
   * Sets the island on which the player is currently located.
   *
   * @param game The island on which the player is currently located.
   */
  public void setGame(Game game) {
    this.game = game;
  }

  /**
   * Returns the total amount of shrimp pounds caught by the player.
   *
   * @return The total amount of shrimp pounds caught by the player.
   */
  public int getShrimpCaught() {
    return this.shrimpCaught;
  }

  /**
   * Sets the total amount of shrimp pounds caught by the player.
   *
   * @param shrimpCaught The total amount of shrimp pounds caught by the player.
   */
  public void setShrimpCaught(int shrimpCaught) {
    this.shrimpCaught = shrimpCaught;
  }
  /**
   * Calculates the profit of the player based on the price of shrimp and the expenses incurred.
   *
   * @param shrimpPrice the price of shrimp per pound
   * @return the calculated profit of the player
   */
  public int calculateProfitValue(int shrimpPrice) {
    return (shrimpPrice - this.expenses);
  }

  public boolean hasCaughtShrimp()
  {
    boolean hasCaughtShrimp = false;
    if (this.shrimpCaught != 0)
    {
      hasCaughtShrimp = true;
    }
    return hasCaughtShrimp;
  }
}
