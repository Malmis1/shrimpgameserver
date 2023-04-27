package org.example.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Island class represents an island where players can catch shrimp.
 * <p>
 * It contains information about the name of the island and the current amount of shrimp kilograms
 * available on the island.
 */
public class Game {
  private final String name;
  private final int number;
  private final List<Player> players;
  private final GameSettings gameSettings;
  private final List<String> messages;
  private final Map<Integer, Round> rounds;
  private int currentRoundNum;

  /**
   * Constructs an Island object with the given name and initial shrimp stock.
   *
   * @param name the name of the island
   */
  public Game(String name, int number, List<Player> players, GameSettings gameSettings) {
    this.name = name;
    this.number = number;
    this.players = players;
    this.gameSettings = gameSettings;
    this.messages = new ArrayList<String>();
    this.rounds = new HashMap<Integer, Round>();
    this.currentRoundNum = 1;
  }

  public Game(Game game)
  {
    this.name = game.getName();
    this.number = game.getNumber();
    this.players = new ArrayList<>(game.getPlayers());
    this.gameSettings = new GameSettings(game.getGameSettings());
    this.messages = new ArrayList<>(game.getMessages());
    this.rounds = new HashMap<>(game.getRounds());
    this.currentRoundNum = game.getCurrentRoundNum();
  }

  /**
   * Returns the name of the island.
   *
   * @return the name of the island
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the unique identifier of the game.
   * 
   * @return the number for the game.
   */
  public int getNumber() {
    return this.number;
  }

  /**
   * Gets the players of the game.
   * 
   * @return a list of all the players of the game.
   */
  public List<Player> getPlayers() {
    return this.players;
  }

  public GameSettings getGameSettings()
  {
    return this.gameSettings;
  }
  /**
   * Gets the messages from the game.
   * 
   * @return the messages between the players during the game.
   */
  public List<String> getMessages()
  {
    return this.messages;
  }

  /**
   * Gets the rounds of the game.
   * 
   * @return the rounds of the game.
   */
  public Map<Integer, Round> getRounds() {
    return this.rounds;
  }

  /**
   * Gets the current round number of the game.
   * 
   * @return the current round of the game as an {@code int} value.
   */
  public int getCurrentRoundNum() {
    return this.currentRoundNum;
  }

  /**
   * Checks if the game contains a specified {@code Player}.
   * 
   * @param player the player to check for.
   * @return {@code true} if the game has the specified player, 
   * or {@code false} if it does not.
   */
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

  /**
   * Stores the current round information to the list of rounds of the game.
   */
  public void storeCurrentRound() {
    Round round = new Round(this.currentRoundNum);
    Map<Player, Integer> playerShrimpCaughtMap = new HashMap<>();
    for (Player player : this.players) {
      playerShrimpCaughtMap.put(player, player.getShrimpCaught());
      player.setShrimpCaught(-1);
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
    round.setPlayerTotalProfitMap(playerMoneyMap);
    this.rounds.put(round.getNumber(), round);
    this.currentRoundNum++;
  }
}
