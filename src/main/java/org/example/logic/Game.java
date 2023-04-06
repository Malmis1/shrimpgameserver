package org.example.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class represents a game with a unique identifier, name, game settings, status, and a
 * list of players.
 * <p>
 * The class provides methods for managing the game, including starting and ending the game,
 * updating player stats,
 * <p>
 * broadcasting messages, sending game results, and disconnecting clients.
 */
public class Game {
  private final String name;
  private final GameSettings settings;
  private String gameStatus;
  private final List<Player> players;
  private final List<Island> islands;

  /**
   * Constructs a new Game object with the specified game ID, name, settings, status, and players.
   *
   * @param name     the name of the game
   * @param settings the settings of the game
   * @param players  the list of players in the game
   */
  public Game(String name, GameSettings settings, List<Player> players) {
    this.name = name;
    this.settings = settings;
    this.players = new ArrayList<>(players);
    List<Player> islandPlayers = new ArrayList<Player>();
    List<Island> islands = new ArrayList<>();
    int islandNum = 1;
    for (Player player : players) {
      if (islandPlayers.isEmpty() || islandPlayers.size() % 3 != 0) {
        islandPlayers.add(player);
        System.out.println("Game class: Added " + player.getName() + " to islandPlayers");
      }
      else {
        Island island = new Island(name, islandNum, islandPlayers);
        for (Player islandPlayer : islandPlayers)
        {
          islandPlayer.setIsland(island);
        }
        islands.add(island);
        islandPlayers.clear();
        islandNum++;
      }
    }
    this.islands = islands;
    System.out.println("Game class: Size of islands in game: " + this.islands.size());
  }

  /**
   * Returns the name of the game.
   *
   * @return the game name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the settings of the game.
   *
   * @return the game settings
   */
  public GameSettings getSettings() {
    return this.settings;
  }

  /**
   * Returns the current status of the game.
   *
   * @return the game status
   */
  public String getGameStatus() {
    return this.gameStatus;
  }

  /**
   * Sets the current status of the game.
   *
   * @param gameStatus the new game status
   */
  public void setGameStatus(String gameStatus) {
    this.gameStatus = gameStatus;
  }

  /**
   * Returns the list of players in the game.
   *
   * @return the list of players
   */
  public List<Player> getPlayers() {
    return this.players;
  }

  public List<Island> getIslands() {
    return this.islands;
  }

  /**
   * Starts the game.
   */
  public void start() {
    // code to start the game
  }

  /**
   * Ends the game.
   */
  public void end() {
    // code to end the game
  }
}
