package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.example.logic.Game;
import org.example.logic.GameSettings;
import org.example.logic.Island;
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
  private final Map<String, Lobby> nameLobbyMap;
  private final Map<String, Game> stringGameMap;
  private final List<ClientHandler> clients;
  private final UsernameCollection usernameCollection;
  private final Map<String, String> ipUsernameMap;
  private final Map<String, Boolean> ipAdminMap;
  private final String adminPassword;
  public static final String VERSION = "1.6.0";

  /**
   * Constructs a new Server object and initializes the lobbies, games, and clients ArrayLists.
   */
  public Server() {
    this.lobbyGameSettingsMap = new HashMap<Lobby, GameSettings>();
    this.nameLobbyMap = new HashMap<String, Lobby>();
    this.stringGameMap = new HashMap<String, Game>();
    this.clients = new ArrayList<ClientHandler>();
    this.usernameCollection = new UsernameCollection();
    this.ipUsernameMap = new HashMap<String, String>();
    this.ipAdminMap = new HashMap<String, Boolean>();
    this.adminPassword = "detteerbra";
  }

  public List<ClientHandler> getClients() {
    return this.clients;
  }

  public UsernameCollection getUsernameCollection() {
    return this.usernameCollection;
  }

  public Map<String, String> getIpUsernameMap() {
    return this.ipUsernameMap;
  }

  public Map<String, Boolean> getIpAdminMap() {
    return this.ipAdminMap;
  }

  public String getAdminPassword() {
    return this.adminPassword;
  }

  /**
   * Returns a list of all available lobbies on the server.
   *
   * @return a list of lobbies
   */
  public Map<Lobby, GameSettings> getLobbyGameSettingsMap() {
    return this.lobbyGameSettingsMap;
  }

  public Map<String, Lobby> getNameLobbyMap() {
    return this.nameLobbyMap;
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
      this.nameLobbyMap.put(lobby.getName(), lobby);
    }
    catch (IllegalArgumentException exception) {
      throw new RuntimeException("Failed to create lobby.");
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
    Player player = clientHandler.getPlayer();
    Iterator<Lobby> iterator = this.lobbyGameSettingsMap.keySet().iterator();
    boolean finished = false;
    while (!finished && iterator.hasNext()) {
      Lobby lobby = iterator.next();
      if (lobby.getName().equals(lobbyName)) {
        lobby.getPlayers().add(player);
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
    Player player = clientHandler.getPlayer();
    Iterator<Lobby> iterator = this.lobbyGameSettingsMap.keySet().iterator();
    boolean finished = false;
    while (!finished && iterator.hasNext()) {
      Lobby lobby = iterator.next();
      if (lobby.hasPlayer(player)) {
        lobby.getPlayers().remove(player);
        finished = true;
      }
    }
  }

  /**
   * Starts the specified game.
   */
  public void startGame(Lobby lobby) {
    GameSettings gameSettings = new GameSettings(this.lobbyGameSettingsMap.get(lobby));
    Game game = new Game(lobby.getName(), gameSettings, lobby.getPlayers());
    this.stringGameMap.put(game.getName(), game);
    for (ClientHandler client : this.getClients()) {
      Player player = client.getPlayer();
      if (lobby.hasPlayer(player)) {
        List<Island> islands = game.getIslands();
        Island playerIsland = null;
        Iterator<Island> iterator = islands.iterator();
        boolean islandFound = false;
        while (!islandFound && iterator.hasNext()) {
          Island island = iterator.next();
          if (island.hasPlayer(player)) {
            playerIsland = island;
            islandFound = true;
          }
        }
        StringBuilder gameStarted = new StringBuilder("UPDATE GAME_STARTED");
        List<Player> otherPlayers = new ArrayList<>(playerIsland.getPlayers());
        otherPlayers.remove(player);
        gameStarted.append(
            " " + otherPlayers.get(0).getName() + " " + otherPlayers.get(1).getName());
        gameStarted.append(" " + gameSettings.getNumberOfRounds());
        gameStarted.append(" " + gameSettings.getRoundTime());
        gameStarted.append(" " + gameSettings.getMinShrimpPounds());
        gameStarted.append(" " + gameSettings.getMaxShrimpPounds());
        gameStarted.append(" " + playerIsland.getNumber());
        gameStarted.append(" " + game.getName());
        client.send(gameStarted.toString());
      }
    }
    this.lobbyGameSettingsMap.remove(lobby, gameSettings);
    this.nameLobbyMap.remove(lobby.getName(), lobby);
    this.sendLobbyInfoToClients();
  }

  /**
   * Ends the specified game and removes it from the games list.
   *
   * @param game the game to end
   */
  public void endGame(Game game) {
    this.stringGameMap.remove(game.getName(), game);
  }

  public void sendLobbyInfoToClients() {
    StringBuilder lobbyList = new StringBuilder("UPDATE LOBBY");
    for (Lobby lobby : this.getLobbyGameSettingsMap().keySet()) {
      String name = lobby.getName();
      String playerAmount = "" + lobby.getPlayers().size();
      String capacity = "" + lobby.getMaxPlayers();
      lobbyList.append(" " + name + "." + playerAmount + "." + capacity);
    }
    for (ClientHandler client : this.getClients()) {
      client.send(lobbyList.toString());
    }
  }

  public void catchShrimp(ClientHandler clientHandler, int shrimpCaught) {
    Player player = clientHandler.getPlayer();
    player.setShrimpCaught(shrimpCaught);
    Island island = player.getIsland();
    List<Player> otherPlayers = new ArrayList<>(island.getPlayers());
    otherPlayers.remove(player);
    for (Player otherPlayer : otherPlayers) {
      ClientHandler client = otherPlayer.getClientHandler();
      this.sendShrimpCaughtInfoToClient(client, player.getName(), shrimpCaught);
    }
  }

  public void sendShrimpCaughtInfoToClient(ClientHandler client, String playerName,
                                           int shrimpCaught) {
    client.send("UPDATE SHRIMP_CAUGHT " + playerName + " " + shrimpCaught);
  }
}

