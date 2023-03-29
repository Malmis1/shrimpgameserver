package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
public class Server
{
    private static final int PORT = 8080;
    private Map<Lobby, GameSettings> lobbies;
    private Map<String, Game> games;
    private Map<Player, ClientHandler> players;
    private List<ClientHandler> clients;
    private UsernameCollection usernameCollection;
    private String adminPassword;

    /**
     * Constructs a new Server object and initializes the lobbies, games, and clients ArrayLists.
     */
    public Server()
    {
        this.lobbies = new HashMap<Lobby, GameSettings>();
        this.games = new HashMap<String, Game>();
        this.players = new HashMap<Player, ClientHandler>();
        this.clients = new ArrayList<ClientHandler>();
        this.usernameCollection = new UsernameCollection();
        this.adminPassword = "adamsmith123";
    }

    public synchronized UsernameCollection getUsernameCollection()
    {
        return this.usernameCollection;
    }

    public String getAdminPassword()
    {
        return this.adminPassword;
    }

    public void start()
    {
        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            System.out.println("Server started on port " + PORT);
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println(
                    "New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        }
        catch (IOException exception)
        {
            System.err.println("Failed to open a server socket: " + exception);
        }
    }

    /**
     * Creates a new lobby with the specified settings and adds it to the lobbies list.
     *
     * @param lobbyName       the name of the lobby
     * @param numPlayers      the maximum number of players allowed in the lobby
     * @param numRounds       the number of rounds in the game
     * @param roundTime       the time limit for each round
     * @param minShrimpPounds the minimum amount of shrimp that can be caught in a round
     * @param maxShrimpPounds the maximum amount of shrimp that can be caught in a round
     */
    public void createLobby(String lobbyName, int numPlayers, int numRounds, int roundTime,
                            int minShrimpPounds, int maxShrimpPounds)
    {
        Lobby lobby = new Lobby(lobbyName);
        GameSettings gameSettings = new GameSettings(numPlayers, numRounds, roundTime,
                                                     minShrimpPounds, maxShrimpPounds);
        this.lobbies.put(lobby, gameSettings);
    }

    /**
     * Returns a list of all available lobbies on the server.
     *
     * @return a list of lobbies
     */
    public Map<Lobby, GameSettings> getLobbies()
    {
        return this.lobbies;
    }

    /**
     * Adds the specified client handler to the lobby with the specified name, creating a new
     * game if the lobby is full.
     *
     * @param clientHandler the client handler to add to the lobby
     * @param lobbyName     the name of the lobby to join
     */
    public void joinLobby(ClientHandler clientHandler, String lobbyName)
    {
        Iterator<Lobby> iterator = this.lobbies.keySet().iterator();
        boolean finished = false;
        while (!finished && iterator.hasNext())
        {
            Lobby lobby = iterator.next();
            if (lobby.getName().equals(lobbyName))
            {
                lobby.addPlayer(clientHandler.getPlayer());
                if (lobby.isFull())
                {
                    Game game = new Game(lobby.getName(), this.lobbies.get(lobby),
                                         lobby.getPlayers());
                    this.startGame(game);
                }
                finished = true;
            }
        }
    }

    /**
     * Starts the specified game.
     *
     * @param game the game to start
     */
    public void startGame(Game game)
    {
        this.games.put(game.getName(), game);
        game.start();
    }

    /**
     * Ends the specified game and removes it from the games list.
     *
     * @param game the game to end
     */
    public void endGame(Game game)
    {
        this.games.remove(game);
    }

    /**
     * Updates the specified player's stats based on their performance in the game.
     *
     * @param player       the player to update
     * @param poundsCaught the amount of shrimp caught by the player
     * @param expenses     the expenses of the player
     */
    public void updatePlayerStats(Player player, int poundsCaught, int expenses)
    {

    }

    /**
     * Broadcasts the specified message to all clients in the specified game.
     *
     * @param message the message to broadcast
     * @param game    the game to broadcast the message to
     */
    public void broadcastMessage(String message, Game game)
    {
        game.broadcastMessage(message);
    }

    /**
     * Sends the final result of a game to all clients in the game.
     *
     * @param game the game whose result should be sent
     */
    public void sendResult(Game game)
    {

    }

    /**
     * Disconnects a client from the server, removing the specified client handler from the
     * clients list
     * and ending any games they are currently in.
     *
     * @param clientHandler the client handler representing the client to be disconnected
     */
    public void disconnectClient(ClientHandler clientHandler)
    {
        this.clients.remove(clientHandler);
        Game game = clientHandler.getGame();
        if (game != null)
        {
            game.end();
        }
    }

    public void addPlayer(Player player, ClientHandler clientHandler)
    {
        this.players.put(player, clientHandler);
    }

    public void sendLobbyCreated(ClientHandler clientHandler, String lobbyName)
    {
        clientHandler.sendMessage("LOBBY_CREATED " + lobbyName);
    }

    public void sendLobbyList(ClientHandler clientHandler, Set<Lobby> lobbies)
    {
        String lobbyList = lobbies.stream().map(Lobby::getName).collect(Collectors.joining(","));
        clientHandler.sendMessage("LOBBY_LIST " + lobbyList);
    }

    public void sendLobbyJoined(ClientHandler clientHandler)
    {
        clientHandler.sendMessage("LOBBY_JOINED");
    }

    public void sendLobbyLeft(ClientHandler clientHandler)
    {
        clientHandler.sendMessage("LOBBY_LEFT");
    }

    public void sendGameStarting(Game game)
    {
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage("GAME_STARTING");
        }
    }

    public void sendRoundStarting(Game game, int roundNumber)
    {
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage("ROUND_STARTING " + roundNumber);
        }
    }

    public void sendRoundEnded(Game game, int shrimpPrice, int income, int expenses)
    {
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage("ROUND_ENDED " + shrimpPrice + " " + income + " " + expenses);
        }
    }

    public void sendCommunicationStarting(Game game)
    {
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage("COMMUNICATION_STARTING");
        }
    }

    public void sendCommunicationEnded(Game game)
    {
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage("COMMUNICATION_ENDED");
        }
    }

    public void sendScoreboard(Game game, int round, int[] shrimpCounts, int totalShrimp,
                               int income, int profit, int totalProfit)
    {
        StringBuilder messageBuilder = new StringBuilder("SCOREBOARD ");
        messageBuilder.append(round);
        for (int shrimpCount : shrimpCounts)
        {
            messageBuilder.append(" ").append(shrimpCount);
        }
        messageBuilder.append(" ").append(totalShrimp);
        messageBuilder.append(" ").append(income);
        messageBuilder.append(" ").append(profit);
        messageBuilder.append(" ").append(totalProfit);
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage(messageBuilder.toString());
        }
    }

    public void sendResults(Game game, int[] scores)
    {
        StringBuilder messageBuilder = new StringBuilder("RESULTS ");
        for (int score : scores)
        {
            messageBuilder.append(score).append(" ");
        }
        for (Player player : game.getPlayers())
        {
            ClientHandler clientHandler = this.players.get(player);
            clientHandler.sendMessage(messageBuilder.toString());
        }
    }

    public void sendError(ClientHandler clientHandler, String errorMessage)
    {
        clientHandler.sendMessage("ERROR " + errorMessage);
    }

    public void sendDisconnected(ClientHandler clientHandler)
    {
        clientHandler.sendMessage("DISCONNECTED");
    }
}

