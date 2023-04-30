package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.example.logic.GameCollection;
import org.example.logic.GameSettings;
import org.example.logic.Game;
import org.example.logic.Lobby;
import org.example.logic.Player;
import org.example.logic.Round;
import org.example.logic.UsernameCollection;

/**
 * The Server class represents the main server application, responsible for creating and managing
 * game lobbies, handling client connections, and coordinating game logic.
 */
public class Server {
  private static final int PORT = 8080;
  public static final String VERSION = "1.7.6";
  private final Map<Lobby, GameSettings> lobbyGameSettingsMap;
  private final Map<String, Lobby> nameLobbyMap;
  private final List<ClientHandler> clients;
  private final List<Game> finishedGames;
  private final UsernameCollection usernameCollection;
  private final Map<String, String> ipUsernameMap;
  private final Map<String, Boolean> ipAdminMap;
  private final String adminPassword;
  private int mostRecentGameIndex;

  /**
   * Constructs a new Server object and initializes the lobbies, games, and clients ArrayLists.
   */
  public Server() {
    this.lobbyGameSettingsMap = new HashMap<Lobby, GameSettings>();
    this.nameLobbyMap = new HashMap<String, Lobby>();
    this.clients = new ArrayList<ClientHandler>();
    this.finishedGames = new ArrayList<>();
    this.usernameCollection = new UsernameCollection();
    this.ipUsernameMap = new HashMap<String, String>();
    this.ipAdminMap = new HashMap<String, Boolean>();
    this.adminPassword = "detteerbra";
    this.mostRecentGameIndex = 0;
  }

  /**
   * Gets the clients of the server.
   *
   * @return a list of all the clients of the server.
   */
  public List<ClientHandler> getClients() {
    return this.clients;
  }

  /**
   * Gets the username collection of the server.
   *
   * @return a collection of usernames.
   */
  public UsernameCollection getUsernameCollection() {
    return this.usernameCollection;
  }

  public Map<String, String> getIpUsernameMap() {
    return this.ipUsernameMap;
  }

  public Map<String, Boolean> getIpAdminMap() {
    return this.ipAdminMap;
  }

  /**
   * Gets the password used for promoting a user to an admin.
   *
   * @return the admin password.
   */
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

  /**
   * Gets a map of lobbies with their respective names.
   *
   * @return a map of lobbies and names of the lobbies.
   */
  public Map<String, Lobby> getNameLobbyMap() {
    return this.nameLobbyMap;
  }

  public List<Game> getFinishedGames() {
    return this.finishedGames;
  }

  public int getMostRecentGameIndex() {
    return this.mostRecentGameIndex;
  }

  public void setMostRecentGameIndex(int mostRecentGameIndex) {
    this.mostRecentGameIndex = mostRecentGameIndex;
  }

  /**
   * Starts the server application.
   */
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
   * @param lobbyName              the name of the lobby
   * @param numPlayers             the maximum number of players allowed in the lobby
   * @param numRounds              the number of rounds in the game
   * @param roundTime              the time limit in seconds for each round
   * @param communicationRounds    the communication rounds of the game
   * @param communicationRoundTime the time (in seconds) during the communication rounds
   * @param minShrimpKilograms     the minimum amount of shrimp that can be caught in a round
   * @param maxShrimpKilograms     the maximum amount of shrimp that can be caught in a round
   * @throws RuntimeException if there is an error creating the lobby, such as if the lobby
   *                          name is null or empty
   */
  public void createLobby(String lobbyName, int numPlayers, int numRounds, int roundTime,
                          String communicationRounds, int communicationRoundTime,
                          int minShrimpKilograms, int maxShrimpKilograms) {
    try {
      Lobby lobby = new Lobby(lobbyName, numPlayers);
      GameSettings gameSettings = new GameSettings(numPlayers, numRounds, roundTime,
                                                   communicationRounds, communicationRoundTime,
                                                   minShrimpKilograms, maxShrimpKilograms);
      this.lobbyGameSettingsMap.put(lobby, gameSettings);
      this.nameLobbyMap.put(lobby.getName(), lobby);
      System.out.println("Created a new lobby called: " + lobbyName + "\r\n");
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
    System.out.println(player.getName() + " joined the lobby " + lobbyName + "\r\n");
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
    String lobbyName = "";
    while (!finished && iterator.hasNext()) {
      Lobby lobby = iterator.next();
      if (lobby.hasPlayer(player)) {
        lobby.getPlayers().remove(player);
        lobbyName = lobby.getName();
        finished = true;
      }
    }
    System.out.println(player.getName() + " left the lobby " + lobbyName + "\r\n");
  }

  /**
   * Starts a specified game.
   *
   * @param lobby the game to start.
   */
  public void startGame(Lobby lobby) {
    GameSettings gameSettings = new GameSettings(this.lobbyGameSettingsMap.get(lobby));
    GameCollection gameCollection = new GameCollection(lobby.getName(), gameSettings,
                                                       lobby.getPlayers());
    for (ClientHandler client : this.getClients()) {
      Player player = client.getPlayer();
      if (lobby.hasPlayer(player)) {
        List<Game> games = gameCollection.getIslands();
        Game playerGame = null;
        Iterator<Game> iterator = games.iterator();
        boolean gameFound = false;
        while (!gameFound && iterator.hasNext()) {
          Game game = iterator.next();
          if (game.hasPlayer(player)) {
            playerGame = game;
            gameFound = true;
          }
        }
        StringBuilder gameStarted = new StringBuilder("UPDATE GAME_STARTED");
        List<Player> otherPlayers = new ArrayList<>(playerGame.getPlayers());
        otherPlayers.remove(player);
        gameStarted.append(
            " " + otherPlayers.get(0).getName() + " " + otherPlayers.get(1).getName());
        gameStarted.append(" " + gameSettings.getNumberOfRounds());
        gameStarted.append(" " + gameSettings.getRoundTime());
        gameStarted.append(" " + gameSettings.getCommunicationRounds());
        gameStarted.append(" " + gameSettings.getCommunicationRoundTime());
        gameStarted.append(" " + gameSettings.getMinShrimpKilograms());
        gameStarted.append(" " + gameSettings.getMaxShrimpKilograms());
        gameStarted.append(" " + playerGame.getNumber());
        gameStarted.append(" " + gameCollection.getName());
        client.send(gameStarted.toString());
      }
    }
    this.lobbyGameSettingsMap.remove(lobby, this.lobbyGameSettingsMap.get(lobby));
    this.nameLobbyMap.remove(lobby.getName(), lobby);
    this.sendLobbyInfoToClients();
    System.out.println("The game " + lobby.getName() + " has started" + "\r\n");
  }

  public synchronized void endGame(Game game) {
    System.out.println("1");
    synchronized (this.finishedGames) {
      System.out.println("2");
      this.getFinishedGames().add(new Game(game));
      System.out.println("3");
      this.setMostRecentGameIndex(this.getFinishedGames().size() - 1);
      System.out.println("4");
      this.sendFinishedGameToAdmins(this.getMostRecentGameIndex());
      System.out.println("5");
    }
  }

  /**
   * Sends information about lobbies to the clients.
   */
  public void sendLobbyInfoToClients() {
    StringBuilder lobbyInfoData = new StringBuilder("UPDATE LOBBY");
    for (Lobby lobby : this.getLobbyGameSettingsMap().keySet()) {
      String name = lobby.getName();
      String playerAmount = "" + lobby.getPlayers().size();
      String capacity = "" + lobby.getMaxPlayers();
      lobbyInfoData.append(" " + name + "." + playerAmount + "." + capacity);
    }
    for (ClientHandler client : this.getClients()) {
      client.send(lobbyInfoData.toString());
    }
    System.out.println("Sent updated lobby list to all clients" + "\r\n");
  }

  /**
   * Catches a specified amount of shrimp.
   *
   * @param clientHandler the clientHandler for the player that catches shrimp.
   * @param shrimpCaught  the amount of shrimp to catch.
   */
  public void catchShrimp(ClientHandler clientHandler, int shrimpCaught) {
    Player player = clientHandler.getPlayer();
    player.setShrimpCaught(shrimpCaught);
    System.out.println(player.getName() + " caught " + shrimpCaught + "kg of shrimp" + "\r\n");
    Game game = player.getGame();
    if (game.allPlayersCaughtShrimp()) {
      game.storeCurrentRound();
      this.sendRoundResultsToClients(game);
      if (game.getGameSettings().getNumberOfRounds() + 1 == game.getCurrentRoundNum()) {
        this.endGame(game);
      }
    }

  }

  public void sendFinishedGameToClient(int finishedGameIndex, ClientHandler client) {
    String finishedGameData = this.getFinishedGameData(finishedGameIndex);
    client.send(finishedGameData.toString());
    System.out.println(
        "Sent finished game data to " + client.getPlayer().getName() + "|" + client.getIpAddress()
        + "\r\n");
  }

  public void sendFinishedGameToAdmins(int finishedGameIndex) {
    for (ClientHandler client : this.getClients()) {
      if (client.getPlayer().isAdmin()) {
        this.sendFinishedGameToClient(finishedGameIndex, client);
      }
    }
  }

  public void sendAllFinishedGamesToClient(ClientHandler clientHandler) {
    synchronized (this.finishedGames) {
      for (Game finishedGame : this.finishedGames) {
        this.sendFinishedGameToClient(this.finishedGames.indexOf(finishedGame), clientHandler);
      }
    }
  }

  public String getFinishedGameData(int finishedGameIndex) {
    StringBuilder finishedGameData = new StringBuilder("UPDATE FINISHED_GAME");
    Game game = this.finishedGames.get(finishedGameIndex);
    GameSettings gameSettings = game.getGameSettings();
    String gameName = game.getName();
    int gameNumber = game.getNumber();
    List<Player> players = game.getPlayers();
    Player player1 = players.get(0);
    Player player2 = players.get(1);
    Player player3 = players.get(2);

    finishedGameData.append(
        " " + gameName + " " + gameNumber + " " + player1.getName() + "." + player2.getName() + "."
        + player3.getName());
    finishedGameData.append(" ");
    Iterator<Round> roundIterator = game.getRounds().values().iterator();
    while (roundIterator.hasNext()) {
      Round round = roundIterator.next();
      Map<Player, Integer> playerShrimpCaughtMap = round.getPlayerShrimpCaughtMap();
      Map<Player, Integer> playerRoundProfitMap = round.getPlayerRoundProfitMap();
      Map<Player, Integer> playerTotalProfitMap = round.getPlayerTotalProfitMap();

      finishedGameData.append(round.getNumber() + "." + playerShrimpCaughtMap.get(player1) + "."
                              + playerShrimpCaughtMap.get(player2) + "."
                              + playerShrimpCaughtMap.get(player3) + "."
                              + round.getTotalShrimpCaught() + "." + round.getShrimpPrice() + "."
                              + (round.getShrimpPrice() - 5) + "." + playerRoundProfitMap.get(
          player1) + "." + playerTotalProfitMap.get(player1) + "." + playerRoundProfitMap.get(
          player2) + "." + playerTotalProfitMap.get(player2) + "." + playerRoundProfitMap.get(
          player3) + "." + playerTotalProfitMap.get(player3));
      if (roundIterator.hasNext()) {
        finishedGameData.append(",");
      }
    }
    finishedGameData.append(
        " " + gameSettings.getNumberOfPlayers() + "." + gameSettings.getNumberOfRounds() + "."
        + gameSettings.getRoundTime() + "." + gameSettings.getCommunicationRounds() + "."
        + gameSettings.getCommunicationRoundTime() + "." + gameSettings.getMinShrimpKilograms()
        + "." + gameSettings.getMaxShrimpKilograms());
    finishedGameData.append(" ");
    if (game.getMessages().size() != 0) {
      for (String message : game.getMessages()) {
        finishedGameData.append(message + "◊");
      }
    }
    else {
      finishedGameData.append("NO_CHAT");
    }

    return finishedGameData.toString();
  }

  /**
   * Sends the round results to all the clients.
   *
   * @param game the game to get the round information from.
   */
  public void sendRoundResultsToClients(Game game) {
    int roundNum = game.getCurrentRoundNum() - 1;
    Round round = game.getRounds().get(roundNum);

    for (Player player : game.getPlayers()) {
      ClientHandler client = player.getClientHandler();
      StringBuilder roundResults = new StringBuilder("UPDATE ROUND_FINISHED");
      List<Player> otherPlayers = new ArrayList<>(game.getPlayers());
      otherPlayers.remove(player);
      roundResults.append(" " + round.getShrimpPrice());
      roundResults.append(
          " " + player.getName() + " " + round.getPlayerShrimpCaughtMap().get(player) + " "
          + round.getPlayerRoundProfitMap().get(player));
      roundResults.append(
          " " + otherPlayers.get(0).getName() + " " + round.getPlayerShrimpCaughtMap()
                                                           .get(otherPlayers.get(0)) + " "
          + round.getPlayerRoundProfitMap().get(otherPlayers.get(0)));
      roundResults.append(
          " " + otherPlayers.get(1).getName() + " " + round.getPlayerShrimpCaughtMap()
                                                           .get(otherPlayers.get(1)) + " "
          + round.getPlayerRoundProfitMap().get(otherPlayers.get(1)));
      client.send(roundResults.toString());
    }
    System.out.println("Round " + roundNum + " of " + game.getName() + " has ended" + "\r\n");
  }

  /**
   * Adds a specified message to the chat.
   *
   * @param clientHandler the clientHandler for the player that sends the message.
   * @param message       the message to add.
   */
  public void addMessageToChat(ClientHandler clientHandler, String message) {
    Player player = clientHandler.getPlayer();
    Game game = player.getGame();
    for (Player gamePlayer : game.getPlayers()) {
      ClientHandler client = gamePlayer.getClientHandler();
      StringBuilder chatMessage = new StringBuilder("UPDATE MESSAGE_SENT");
      Date now = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.HOUR_OF_DAY, 2);
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String formattedTime = dateFormat.format(calendar.getTime());
      chatMessage.append(" " + player.getName() + " " + message + " " + formattedTime);
      client.send(chatMessage.toString());
    }
    Date now = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    calendar.add(Calendar.HOUR_OF_DAY, 2);
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    String formattedTime = dateFormat.format(calendar.getTime());
    game.getMessages().add(player.getName() + "☐" + message + "☐" + formattedTime);
    System.out.println(
        player.getName() + " sent (" + message.replace("⁞", " ") + ") to the chat at "
        + formattedTime + "\r\n");
  }
}

