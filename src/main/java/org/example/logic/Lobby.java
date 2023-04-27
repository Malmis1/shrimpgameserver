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
    private int maxPlayers;

    /**
     * Creates a new lobby with the given name.
     *
     * @param name the name of the lobby
     * @param maxPlayers the maximum allowable amount of players in the lobby
     * @throws IllegalArgumentException if the name is null or empty
     */
    public Lobby(String name, int maxPlayers)
    {
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Lobby name cannot be null or empty.");
        }
        this.name = name;
        this.maxPlayers = maxPlayers;
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
     * Gets the maximum amount of players allowed in the lobby.
     * 
     * @return an {@code int} representing the maximum amount of players allowed in the lobby.
     */
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    /**
     * Checks if the lobby is filled with the maximum amount of players.
     * 
     * @return {@code true} if the lobby is full, or {@code false} if it is not.
     */
    public boolean isFull()
    {
        boolean isFull = false;
        if (this.players.size() == this.maxPlayers)
        {
            isFull = true;
        }
        return isFull;
    }

    /**
     * Checks if the lobby has a specified {@code Player}.
     * 
     * @param player the player to check for.
     * @return {@code true} if the player is in the lobby, or {@code false} if not.
     */
    public boolean hasPlayer(Player player)
    {
        return this.players.contains(player);
    }
}
