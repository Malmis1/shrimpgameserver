package org.example.logic;

import java.util.Map;

public class Round {
  private final int number;
  private int shrimpPrice;
  private Map<Player, Integer> playerShrimpCaughtMap;
  private Map<Player, Integer> playerMoneyMap;
  private Map<Player, Integer> playerRoundProfitMap;

  public Round(int number) {
    this.number = number;
  }

  public int getNumber() {
    return this.number;
  }

  public Map<Player, Integer> getPlayerShrimpCaughtMap() {
    return this.playerShrimpCaughtMap;
  }

  public void setPlayerShrimpCaughtMap(Map<Player, Integer> playerShrimpCaughtMap) {
    this.playerShrimpCaughtMap = playerShrimpCaughtMap;
  }

  public Map<Player, Integer> getPlayerMoneyMap()
  {
    return this.playerMoneyMap;
  }

  public void setPlayerMoneyMap(Map<Player, Integer> playerMoneyMap)
  {
    this.playerMoneyMap = playerMoneyMap;
  }

  public Map<Player, Integer> getPlayerRoundProfitMap()
  {
    return this.playerRoundProfitMap;
  }

  public void setPlayerRoundProfitMap(Map<Player, Integer> playerRoundProfitMap)
  {
    this.playerRoundProfitMap = playerRoundProfitMap;
  }

  public int getShrimpPrice() {
    return this.shrimpPrice;
  }

  public void calculateShrimpPrice()
  {
    int totalShrimp = 0;
    for (int shrimpCaught : this.playerShrimpCaughtMap.values()) {
      totalShrimp += shrimpCaught;
    }
    this.shrimpPrice = 45 - (int) (0.2 * totalShrimp);
  }
}
