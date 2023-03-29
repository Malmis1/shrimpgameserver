package org.example.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * The Lobby class represents a lobby where players can join and wait for a game to start.
 * <p>
 * It contains information such as the lobby ID, name, and players in the lobby.
 */
public class Lobby
{
    private String name;
    private List<Player> players;

    /**
     * Constructor for the Lobby class.
     *
     * @param name the name of the lobby
     */
    public Lobby(String name)
    {
        this.name = name;
        this.players = new ArrayList<Player>();
    }

    /**
     * Returns the lobby name.
     *
     * @return the lobby name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the lobby name.
     *
     * @param name the name of the lobby
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the list of players in the lobby.
     *
     * @return the list of players in the lobby
     */
    public List<Player> getPlayers()
    {
        return this.players;
    }

    /**
     * Adds a player to the lobby.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player)
    {
        this.players.add(player);
    }

    /**
     * Removes a player from the lobby.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player)
    {
        this.players.remove(player);
    }

    /**
     * Removes all players from the lobby.
     */
    public void clearPlayers()
    {
        this.players.clear();
    }

    public boolean isFull()
    {
        boolean isFull = false;
        if (this.players.size() % 3 == 0)
        {
            isFull = true;
        }
        return isFull;
    }
}
