package org.example.logic;

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
public class Game
{
    private String name;
    private GameSettings settings;
    private String gameStatus;
    private List<Player> players;

    /**
     * Constructs a new Game object with the specified game ID, name, settings, status, and players.
     *
     * @param name       the name of the game
     * @param settings   the settings of the game
     * @param players    the list of players in the game
     */
    public Game(String name, GameSettings settings, List<Player> players)
    {
        this.name = name;
        this.settings = settings;
        this.players = players;
    }

    /**
     * Returns the name of the game.
     *
     * @return the game name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the game.
     *
     * @param name the new game name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the settings of the game.
     *
     * @return the game settings
     */
    public GameSettings getSettings()
    {
        return this.settings;
    }

    /**
     * Sets the settings of the game.
     *
     * @param settings the new game settings
     */
    public void setSettings(GameSettings settings)
    {
        this.settings = settings;
    }

    /**
     * Returns the current status of the game.
     *
     * @return the game status
     */
    public String getGameStatus()
    {
        return this.gameStatus;
    }

    /**
     * Sets the current status of the game.
     *
     * @param gameStatus the new game status
     */
    public void setGameStatus(String gameStatus)
    {
        this.gameStatus = gameStatus;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return the list of players
     */
    public List<Player> getPlayers()
    {
        return this.players;
    }

    /**
     * Sets the list of players in the game.
     *
     * @param players the new list of players
     */
    public void setPlayers(List<Player> players)
    {
        this.players = players;
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to be added to the game
     */
    public void addPlayer(Player player)
    {
        this.players.add(player);
    }

    /**
     * Removes a player from the game.
     *
     * @param player the player to be removed from the game
     */
    public void removePlayer(Player player)
    {
        this.players.remove(player);
    }

    /**
     * Starts the game.
     */
    public void start()
    {
        // code to start the game
    }

    /**
     * Ends the game.
     */
    public void end()
    {
        // code to end the game
    }

    /**
     * Updates the stats of all players in the game.
     */
    public void updatePlayerStats()
    {
        // code to update player stats
    }

    /**
     * Broadcasts a message to all players in the game.
     *
     * @param message the message to be sent to all players
     */
    public void broadcastMessage(String message)
    {
        // code to send message to all players
    }

    /**
     * Sends the final result of the game to all players in the game.
     */
    public void sendGameResults()
    {
        // code to send game results to all players
    }

    /**
     * Disconnects all clients from the game.
     */
    public void disconnectClients()
    {
        // code to disconnect all clients from the game
    }
}
