package org.example.logic;

/**
 * The GameSettings class represents the settings of a game.
 * <p>
 * It contains information such as the number of players, number of rounds, 
 * round time, communication rounds, communication round time, minimum shrimp kilograms to catch, 
 * and maximum shrimp kilograms to catch.
 */
public class GameSettings {
  private int numberOfPlayers;
  private int numberOfRounds;
  private int roundTime;
  private String communicationRounds;
  private int communicationRoundTime;
  private int minShrimpKilograms;
  private int maxShrimpKilograms;

  /**
   * Constructor for the GameSettings class.
   *
   * @param numberOfPlayers the number of players in the game.
   * @param numberOfRounds  the number of rounds in the game.
   * @param roundTime       the time (in seconds) for each round.
   * @param communicationRounds the communication rounds of the game.
   * @param communicationRoundTime the time (in seconds) for communication rounds.
   * @param minShrimpKilograms the minimum amount of shrimp kilograms that can be caught in a round.
   * @param maxShrimpKilograms the maximum amount of shrimp kilograms that can be caught in a round.
   */
  public GameSettings(int numberOfPlayers, int numberOfRounds, int roundTime,
                      String communicationRounds, int communicationRoundTime, int minShrimpKilograms,
                      int maxShrimpKilograms) {
    this.numberOfPlayers = numberOfPlayers;
    this.numberOfRounds = numberOfRounds;
    this.roundTime = roundTime;
    this.communicationRounds = communicationRounds;
    this.communicationRoundTime = communicationRoundTime;
    this.minShrimpKilograms = minShrimpKilograms;
    this.maxShrimpKilograms = maxShrimpKilograms;
  }

  /**
   * Creates a new instance of {@code GameSettings} from a preexisting instance.
   * 
   * @param gameSettings the {@code GameSettings} to copy.
   */
  public GameSettings(GameSettings gameSettings) {
    this.numberOfPlayers = gameSettings.getNumberOfPlayers();
    this.numberOfRounds = gameSettings.getNumberOfRounds();
    this.roundTime = gameSettings.getRoundTime();
    this.communicationRounds = gameSettings.getCommunicationRounds();
    this.communicationRoundTime = gameSettings.getCommunicationRoundTime();
    this.minShrimpKilograms = gameSettings.getMinShrimpKilograms();
    this.maxShrimpKilograms = gameSettings.getMaxShrimpKilograms();
  }

  /**
   * Getter for the number of players in the game.
   *
   * @return the number of players in the game.
   */
  public int getNumberOfPlayers() {
    return this.numberOfPlayers;
  }

  /**
   * Setter for the number of players in the game.
   *
   * @param numberOfPlayers the number of players in the game.
   */
  public void setNumberOfPlayers(int numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
  }

  /**
   * Getter for the number of rounds in the game.
   *
   * @return the number of rounds in the game.
   */
  public int getNumberOfRounds() {
    return this.numberOfRounds;
  }

  /**
   * Setter for the number of rounds in the game.
   *
   * @param numberOfRounds the number of rounds in the game.
   */
  public void setNumberOfRounds(int numberOfRounds) {
    this.numberOfRounds = numberOfRounds;
  }

  /**
   * Getter for the time (in seconds) for each round.
   *
   * @return the time (in seconds) for each round.
   */
  public int getRoundTime() {
    return this.roundTime;
  }

  /**
   * Setter for the time (in seconds) for each round.
   *
   * @param roundTime the time (in seconds) for each round.
   */
  public void setRoundTime(int roundTime) {
    this.roundTime = roundTime;
  }

  /**
   * Gets the communication rounds of the game.
   * 
   * @return all the communication rounds as a {@code String}.
   */
  public String getCommunicationRounds()
  {
    return this.communicationRounds;
  }

  /**
   * Sets the communication rounds of the game.
   * 
   * @param communicationRounds the communication rounds in the format: "4,6".
   */
  public void setCommunicationRounds(String communicationRounds)
  {
    this.communicationRounds = communicationRounds;
  }

  /**
   * Gets the communication round time.
   * 
   * @return the communication round time (in seconds).
   */
  public int getCommunicationRoundTime()
  {
    return this.communicationRoundTime;
  }

  /**
   * Sets the communication round time.
   * 
   * @param communicationRoundTime the time (in seconds) during the communication rounds.
   */
  public void setCommunicationRoundTime(int communicationRoundTime)
  {
    this.communicationRoundTime = communicationRoundTime;
  }

  /**
   * Getter for the minimum amount of shrimp kilograms that can be caught in a round.
   *
   * @return the minimum amount of shrimp kilograms that can be caught in a round.
   */
  public int getMinShrimpKilograms() {
    return this.minShrimpKilograms;
  }

  /**
   * Setter for the minimum amount of shrimp kilograms that can be caught in a round.
   *
   * @param minShrimpKilograms the minimum amount of shrimp kilograms that can be caught in a round.
   */
  public void setMinShrimpKilograms(int minShrimpKilograms) {
    this.minShrimpKilograms = minShrimpKilograms;
  }

  /**
   * Getter for the maximum amount of shrimp kilograms that can be caught in a round.
   *
   * @return the maximum amount of shrimp kilograms that can be caught in a round.
   */
  public int getMaxShrimpKilograms() {
    return this.maxShrimpKilograms;
  }

  /**
   * Sets the maximum number of shrimp kilograms that can be caught in a round.
   *
   * @param maxShrimpKilograms the maximum number of shrimp kilograms that can be caught in a round.
   */
  public void setMaxShrimpKilograms(int maxShrimpKilograms) {
    this.maxShrimpKilograms = maxShrimpKilograms;
  }
}
