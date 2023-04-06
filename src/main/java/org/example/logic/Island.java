package org.example.logic;

import java.util.List;

/**
 * The Island class represents an island where players can catch shrimp.
 * <p>
 * It contains information about the name of the island and the current amount of shrimp pounds
 * available on the island.
 */
public class Island {
  private String name;
  private int number;
  private List<Player> players;

  /**
   * Constructs an Island object with the given name and initial shrimp stock.
   *
   * @param name the name of the island
   */
  public Island(String name, int number, List<Player> players) {
    this.name = name;
    this.number = number;
    this.players = players;
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

  public boolean hasPlayer(Player player)
  {
    return this.players.contains(player);
  }
}
