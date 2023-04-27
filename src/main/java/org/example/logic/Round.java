package org.example.logic;

import java.util.Map;

/**
 * Represents a round with a number, shrimp price and information about the players actions.
 */
public class Round {
  private final int number;
  private int shrimpPrice;
  private int totalShrimpCaught;
  private Map<Player, Integer> playerShrimpCaughtMap;
  private Map<Player, Integer> playerTotalProfitMap;
  private Map<Player, Integer> playerRoundProfitMap;

  /**
   * Creates a new instance of {@code Round} with a specified round number.
   * @param number
   */
  public Round(int number) {
    this.number = number;
  }

  /**
   * Gets the round number.
   * 
   * @return the number of the round.
   */
  public int getNumber() {
    return this.number;
  }

  /**
   * Gets the shrimp caught per player.
   * 
   * @return a map of the amounts of shrimp each player has caught.
   */
  public Map<Player, Integer> getPlayerShrimpCaughtMap() {
    return this.playerShrimpCaughtMap;
  }

  /**
   * Sets the shrimp caught per player.
   * 
   * @param playerShrimpCaughtMap a specified map of the amounts 
   * of shrimp each player has caught.
   */
  public void setPlayerShrimpCaughtMap(Map<Player, Integer> playerShrimpCaughtMap) {
    this.playerShrimpCaughtMap = playerShrimpCaughtMap;
  }

  /**
   * Gets the money per player.
   * 
   * @return a map of amounts of money each player has.
   */
  public Map<Player, Integer> getPlayerTotalProfitMap()
  {
    return this.playerTotalProfitMap;
  }

  /**
   * Sets the money per player.
   * 
   * @param playerTotalProfitMap a specified map of amounts of
   * money each player has.
   */
  public void setPlayerTotalProfitMap(Map<Player, Integer> playerTotalProfitMap)
  {
    this.playerTotalProfitMap = playerTotalProfitMap;
  }

  /**
   * Gets the round profit per player.
   * 
   * @return a map of amounts of round profit each player has.
   */
  public Map<Player, Integer> getPlayerRoundProfitMap()
  {
    return this.playerRoundProfitMap;
  }

  /**
   * Sets the round profit per player.
   * 
   * @param playerRoundProfitMap a specified map of amounts of 
   * round profit each player has.
   */
  public void setPlayerRoundProfitMap(Map<Player, Integer> playerRoundProfitMap)
  {
    this.playerRoundProfitMap = playerRoundProfitMap;
  }

  /**
   * Gets the shrimp price of the round.
   * 
   * @return the shrimp price.
   */
  public int getShrimpPrice() {
    return this.shrimpPrice;
  }

  public int getTotalShrimpCaught()
  {
    return this.totalShrimpCaught;
  }

  /**
   * Calculates the shrimp price for the round using a predefined formula.
   */
  public void calculateShrimpPrice()
  {
    int totalShrimp = 0;
    for (int shrimpCaught : this.playerShrimpCaughtMap.values()) {
      totalShrimp += shrimpCaught;
    }
    this.totalShrimpCaught = totalShrimp;
    this.shrimpPrice = 45 - (int) (0.2 * totalShrimp);
  }
}
