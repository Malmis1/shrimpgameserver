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
public class GameCollection {
  private final String name;
  private final GameSettings settings;
  private String gameStatus;
  private final List<Player> players;
  private final List<Game> games;

  /**
   * Constructs a new Game object with the specified game ID, name, settings, status, and players.
   *
   * @param name     the name of the game
   * @param settings the settings of the game
   * @param players  the list of players in the game
   */
  public GameCollection(String name, GameSettings settings, List<Player> players) {
    this.name = name;
    this.settings = settings;
    this.players = new ArrayList<>(players);
    List<Player> gamePlayers = new ArrayList<Player>();
    List<Game> games = new ArrayList<>();
    int gameNum = 1;
    for (Player player : players) {
      if (gamePlayers.isEmpty() || gamePlayers.size() % 3 != 0) {
        gamePlayers.add(player);
      }
      if (gamePlayers.size() % 3 == 0) {
        Game game = new Game(name, gameNum, new ArrayList<>(gamePlayers));
        for (Player gamePlayer : gamePlayers) {
          gamePlayer.setGame(game);
        }
        games.add(game);
        gamePlayers.clear();
        gameNum++;
      }
    }
    this.games = games;
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

  public List<Game> getIslands() {
    return this.games;
  }


}
