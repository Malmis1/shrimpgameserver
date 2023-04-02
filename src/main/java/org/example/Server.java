package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.logic.Game;
import org.example.logic.GameSettings;
import org.example.logic.Lobby;
import org.example.logic.Player;
import org.example.logic.UsernameCollection;

/**
 * The Server class represents the main server application, responsible for creating and managing
 * game lobbies, handling client connections, and coordinating game logic.
 */
public class Server {
  private static final int PORT = 8080;
  private final Map<Lobby, GameSettings> lobbyGameSettingsMap;
  private Map<String, Game> stringGameMap;
  private final Map<String, Lobby> stringLobbyMap;
  private Map<ClientHandler, Player> clientHandlerPlayerMap;
  private UsernameCollection usernameCollection;
  private Map<String, String> ipUsernameMap;
  private Map<String, Boolean> ipAdminMap;
  private String adminPassword;

  /**
   * Constructs a new Server object and initializes the lobbies, games, and clients ArrayLists.
   */
  public Server() {
    this.lobbyGameSettingsMap = new HashMap<Lobby, GameSettings>();
    this.stringGameMap = new HashMap<String, Game>();
    this.stringLobbyMap = new HashMap<String, Lobby>();
    this.clientHandlerPlayerMap = new HashMap<ClientHandler, Player>();
    this.usernameCollection = new UsernameCollection();
    this.ipUsernameMap = new HashMap<String, String>();
    this.ipAdminMap = new HashMap<String, Boolean>();
    this.adminPassword = "detteerbra";
  }

  public synchronized Map<ClientHandler, Player> getClientHandlerPlayerMap() {
    return this.clientHandlerPlayerMap;
  }

  public synchronized UsernameCollection getUsernameCollection() {
    return this.usernameCollection;
  }

  public synchronized Map<String, String> getIpUsernameMap() {
    return this.ipUsernameMap;
  }

  public synchronized Map<String, Boolean> getIpAdminMap() {
    return this.ipAdminMap;
  }

  public String getAdminPassword() {
    return this.adminPassword;
  }

  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Server started on port " + PORT + "\r\n");
      while (true) {
        Socket clientSocket = serverSocket.accept();
        ClientHandler clientHandler = new ClientHandler(clientSocket, this);
        Thread clientThread = new Thread(clientHandler);
        clientThread.start();
      }
    }
    catch (IOException exception) {
      System.err.println("Failed to open a server socket: " + exception);
    }
  }

  /**
   * Creates a new lobby with the specified settings and adds it to the lobbies list.
   *
   * @param lobbyName       the name of the lobby
   * @param numPlayers      the maximum number of players allowed in the lobby
   * @param numRounds       the number of rounds in the game
   * @param roundTime       the time limit in seconds for each round
   * @param minShrimpPounds the minimum amount of shrimp that can be caught in a round
   * @param maxShrimpPounds the maximum amount of shrimp that can be caught in a round
   * @throws RuntimeException if there is an error creating the lobby, such as if the lobby
   *                          name is null or empty
   */
  public void createLobby(String lobbyName, int numPlayers, int numRounds, int roundTime,
                          int minShrimpPounds, int maxShrimpPounds) {
    try {
      Lobby lobby = new Lobby(lobbyName, numPlayers);
      GameSettings gameSettings = new GameSettings(numPlayers, numRounds, roundTime,
                                                   minShrimpPounds, maxShrimpPounds);
      this.lobbyGameSettingsMap.put(lobby, gameSettings);
      this.stringLobbyMap.put(lobbyName, lobby);
    }
    catch (IllegalArgumentException exception) {
      throw new RuntimeException("Failed to create lobby.");
    }
  }

  /**
   * Returns a list of all available lobbies on the server.
   *
   * @return a list of lobbies
   */
  public Map<Lobby, GameSettings> getLobbyGameSettingsMap() {
    synchronized (this.lobbyGameSettingsMap) {
      return this.lobbyGameSettingsMap;
    }
  }

  public Map<String, Lobby> getStringLobbyMap() {
    synchronized (this.stringLobbyMap) {
      return this.stringLobbyMap;
    }
  }

  /**
   * Adds the specified client handler to the lobby with the specified name, creating a new
   * game if the lobby is full.
   *
   * @param clientHandler the client handler to add to the lobby
   * @param lobbyName     the name of the lobby to join
   */
  public void joinLobby(ClientHandler clientHandler, String lobbyName) {
    Iterator<Lobby> iterator = this.lobbyGameSettingsMap.keySet().iterator();
    boolean finished = false;
    while (!finished && iterator.hasNext()) {
      Lobby lobby = iterator.next();
      if (lobby.getName().equals(lobbyName)) {
        lobby.addPlayer(clientHandler.getPlayer());
        finished = true;
      }
    }
  }

  /**
   * Removes a player from the lobby they are currently in.
   *
   * @param clientHandler the client handler associated with the player to be removed
   */
  public void leaveLobby(ClientHandler clientHandler) {
    Player player = this.clientHandlerPlayerMap.get(clientHandler);
    Iterator<Lobby> iterator = this.lobbyGameSettingsMap.keySet().iterator();
    boolean finished = false;
    while (!finished && iterator.hasNext()) {
      Lobby lobby = iterator.next();
      if (lobby.hasPlayer(player)) {
        lobby.removePlayer(player);
        finished = true;
      }
    }
  }

  /**
   * Starts the specified game.
   *
   * @param game the game to start
   */
  public void startGame(Game game) {
    this.stringGameMap.put(game.getName(), game);
    game.start();
  }

  /**
   * Ends the specified game and removes it from the games list.
   *
   * @param game the game to end
   */
  public void endGame(Game game) {
    this.stringGameMap.remove(game);
  }

  /**
   * Updates the specified player's stats based on their performance in the game.
   *
   * @param player       the player to update
   * @param poundsCaught the amount of shrimp caught by the player
   * @param expenses     the expenses of the player
   */
  public void updatePlayerStats(Player player, int poundsCaught, int expenses) {

  }

  /**
   * Broadcasts the specified message to all clients in the specified game.
   *
   * @param message the message to broadcast
   * @param game    the game to broadcast the message to
   */
  public void broadcastMessage(String message, Game game) {
    game.broadcastMessage(message);
  }

  public void sendLobbyInfoToClients() {
    StringBuilder lobbyList = new StringBuilder("UPDATE LOBBY");
    for (Lobby lobby : this.getLobbyGameSettingsMap().keySet()) {
      String name = lobby.getName();
      String playerAmount = "" + lobby.getPlayers().size();
      String capacity = "" + lobby.getMaxPlayers();
      lobbyList.append(" " + name + "." + playerAmount + "." + capacity);
    }
    for (ClientHandler client : this.getClientHandlerPlayerMap().keySet()) {
      client.send(lobbyList.toString());
    }
  }

  public void addPlayer(ClientHandler clientHandler, Player player) {
    this.clientHandlerPlayerMap.put(clientHandler, player);
  }

  public void sendLobbyCreated(ClientHandler clientHandler, String lobbyName) {
  }

  public void sendLobbyList(ClientHandler clientHandler, Set<Lobby> lobbies) {
    String lobbyList = lobbies.stream().map(Lobby::getName).collect(Collectors.joining(","));

  }

  public void sendLobbyJoined(ClientHandler clientHandler) {
  }

  public void sendLobbyLeft(ClientHandler clientHandler) {
  }

  public void sendGameStarting(Game game) {
  }

  public void sendRoundStarting(Game game, int roundNumber) {
  }

  public void sendRoundEnded(Game game, int shrimpPrice, int income, int expenses) {
  }

  public void sendCommunicationStarting(Game game) {
  }

  public void sendCommunicationEnded(Game game) {
  }

  public void sendScoreboard(Game game, int round, int[] shrimpCounts, int totalShrimp, int income,
                             int profit, int totalProfit) {
    StringBuilder messageBuilder = new StringBuilder("SCOREBOARD ");
    messageBuilder.append(round);
    for (int shrimpCount : shrimpCounts) {
      messageBuilder.append(" ").append(shrimpCount);
    }
    messageBuilder.append(" ").append(totalShrimp);
    messageBuilder.append(" ").append(income);
    messageBuilder.append(" ").append(profit);
    messageBuilder.append(" ").append(totalProfit);
  }

  public void sendResults(Game game, int[] scores) {
    StringBuilder messageBuilder = new StringBuilder("RESULTS ");
    for (int score : scores) {
      messageBuilder.append(score).append(" ");
    }
  }

  public void sendError(ClientHandler clientHandler, String errorMessage) {
  }

  public void sendDisconnected(ClientHandler clientHandler) {
  }
}

