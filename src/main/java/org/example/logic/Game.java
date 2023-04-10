package org.example.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Island class represents an island where players can catch shrimp.
 * <p>
 * It contains information about the name of the island and the current amount of shrimp pounds
 * available on the island.
 */
public class Game {
  private final String name;
  private final int number;
  private final List<Player> players;
  private final List<String> messages;
  private final Map<Integer, Round> rounds;
  private int currentRoundNum;

  /**
   * Constructs an Island object with the given name and initial shrimp stock.
   *
   * @param name the name of the island
   */
  public Game(String name, int number, List<Player> players) {
    this.name = name;
    this.number = number;
    this.players = players;
    this.messages = new ArrayList<String>();
    this.rounds = new HashMap<Integer, Round>();
    this.currentRoundNum = 1;
  }

  /**
   * Returns the name of the island.
   *
   * @return the name of the island
   */
  public String getName() {
    return this.name;
  }

  public int getNumber() {
    return this.number;
  }

  public List<Player> getPlayers() {
    return this.players;
  }

  public List<String> getMessages()
  {
    return this.messages;
  }

  public Map<Integer, Round> getRounds() {
    return this.rounds;
  }

  public int getCurrentRoundNum() {
    return this.currentRoundNum;
  }

  public boolean hasPlayer(Player player) {
    return this.players.contains(player);
  }

  public boolean allPlayersCaughtShrimp() {
    boolean allPlayersCaughtShrimp = true;
    for (Player player : this.players) {
      if (!player.hasCaughtShrimp()) {
        allPlayersCaughtShrimp = false;
      }
    }
    return allPlayersCaughtShrimp;
  }

  public void storeCurrentRound() {
    Round round = new Round(this.currentRoundNum);
    Map<Player, Integer> playerShrimpCaughtMap = new HashMap<>();
    for (Player player : this.players) {
      playerShrimpCaughtMap.put(player, player.getShrimpCaught());
      player.setShrimpCaught(0);
    }
    round.setPlayerShrimpCaughtMap(playerShrimpCaughtMap);
    round.calculateShrimpPrice();
    int shrimpPrice = round.getShrimpPrice();
    Map<Player, Integer> playerRoundProfitMap = new HashMap<>();
    for (Player player : this.players)
    {
      int roundProfit =
          player.calculateProfitValue(shrimpPrice) * playerShrimpCaughtMap.get(player);
      playerRoundProfitMap.put(player, roundProfit);
    }
    round.setPlayerRoundProfitMap(playerRoundProfitMap);
    Map<Player, Integer> playerMoneyMap = new HashMap<>();
    for (Player player : this.players) {
      int profit = player.calculateProfitValue(shrimpPrice) * playerShrimpCaughtMap.get(player);
      int money = player.getMoney() + profit;
      player.setMoney(money);
      playerMoneyMap.put(player, money);
    }
    round.setPlayerMoneyMap(playerMoneyMap);
    this.rounds.put(round.getNumber(), round);
    this.currentRoundNum++;
  }
}
