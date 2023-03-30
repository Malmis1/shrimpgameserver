package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import org.example.logic.Game;
import org.example.logic.Lobby;
import org.example.logic.Player;

/**
 * The ClientHandler class represents a handler for communication between the server and a client.
 * <p>
 * It is responsible for sending and receiving data, processing requests, and handling errors.
 */
public class ClientHandler implements Runnable
{
    private final Socket clientSocket;
    private final Server server;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private Game game;
    private Player player;

    /**
     * Constructor for the ClientHandler class.
     *
     * @param socket a Socket object representing the socket used to communicate with the client
     */
    public ClientHandler(Socket socket, Server server)
    {
        this.clientSocket = socket;
        this.server = server;
        try
        {
            this.bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(this.clientSocket.getOutputStream()));
            this.bufferedReader = new BufferedReader(
                new InputStreamReader(this.clientSocket.getInputStream()));
        }
        catch (IOException exception)
        {
            System.err.println("Failed to open stream: " + exception);
        }
    }

    /**
     * Sends a message to the client through the established connection.
     *
     * @param message the message to be sent
     * @throws RuntimeException if there is a failure to send the message to the client
     */
    private void send(String message)
    {
        try
        {
            bufferedWriter.write(message + "\r\n");
            bufferedWriter.flush();
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Failed to send message to the client.");
        }
    }


    /**
     * Receives a message from the client through the established connection.
     *
     * @return the message received from the client
     * @throws RuntimeException if there is a failure to receive the message from the client
     */
    private String receive()
    {
        try
        {
            return this.bufferedReader.readLine();
        }
        catch (SocketException exception)
        {
            throw new ClientDisconnectedException();
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Failed to receive message from the client.");
        }
    }

    @Override
    public void run()
    {
        boolean isRunning = true;
        while (isRunning)
        {
            String ip = this.clientSocket.getInetAddress().getHostAddress();
            Map<String, String> ipUsernameMap = this.server.getIpUsernameMap();
            boolean isAdmin = this.server.getIpAdminMap().containsKey(ip);
            try
            {
                String[] input;
                String lobbyName;
                input = this.receive().split(" ");
                switch (input[0])
                {
                    case "REQUEST_USERNAME":

                        if (ipUsernameMap.containsKey(ip))
                        {
                            this.send("USERNAME " + ipUsernameMap.get(ip) + " " + isAdmin);
                            System.out.println(
                                this.server.getIpUsernameMap().get(ip) + "|" + ip + " reconnected."
                                + "\r\n");
                        }
                        else
                        {
                            String username =
                                this.server.getUsernameCollection().getRandomUsername();
                            ipUsernameMap.put(ip, username);
                            this.send("USERNAME " + username + " " + isAdmin);
                            this.player = new Player(username, 0, 5);
                            this.server.addPlayer(this, this.player);
                            System.out.println(
                                "Gave new client " + ip + " the username: " + username + "\r\n");
                        }
                        break;

                    case "BECOME_ADMIN":
                        String password = input[1];
                        if (password.equals(this.server.getAdminPassword()))
                        {
                            this.send("BECOME_ADMIN_SUCCESSFUL");
                            this.server.getIpAdminMap().put(ip, true);
                            System.out.println(this.server.getIpUsernameMap().get(ip) + "|" + ip
                                               + " entered the correct admin password and became "
                                               + "administrator." + "\r\n");
                        }
                        else
                        {
                            this.send("BECOME_ADMIN_FAILED");
                            System.out.println(this.server.getIpUsernameMap().get(ip) + "|" + ip
                                               + " tried to become administrator, but entered the"
                                               + " wrong admin password and failed." + "\r\n");
                        }
                        break;

                    case "CREATE_LOBBY":
                        lobbyName = input[1];
                        int numberOfPlayers;
                        int numberOfRounds;
                        int roundTime;
                        int minShrimpPounds;
                        int maxShrimpPounds;
                        try
                        {
                            numberOfPlayers = Integer.parseInt(input[2]);
                            numberOfRounds = Integer.parseInt(input[3]);
                            roundTime = Integer.parseInt(input[4]);
                            minShrimpPounds = Integer.parseInt(input[5]);
                            maxShrimpPounds = Integer.parseInt(input[6]);
                            this.server.createLobby(lobbyName, numberOfPlayers, numberOfRounds,
                                                    roundTime, minShrimpPounds, maxShrimpPounds);
                            this.send("CREATE_LOBBY_SUCCESS");
                            System.out.println(this.server.getIpUsernameMap().get(ip) + "|" + ip
                                               + " created a new lobby called: " + lobbyName
                                               + "\r\n");
                        }
                        catch (NumberFormatException exception)
                        {
                            this.send("CREATE_LOBBY_FAILED");
                            throw new RuntimeException(
                                "Invalid user input received when trying to create lobby.");
                        }
                        catch (RuntimeException exception)
                        {
                            this.send("CREATE_LOBBY_FAILED");
                            throw new RuntimeException(exception.getMessage());
                        }
                        break;

                    case "REQUEST_LOBBY_LIST":
                        StringBuilder lobbyList = new StringBuilder("LOBBY_LIST");
                        try
                        {
                            for (Lobby lobby : this.server.getLobbies().keySet())
                            {
                                String name = lobby.getName();
                                String playerAmount = "" + lobby.getPlayers().size();
                                String capacity = "" + lobby.getMaxPlayers();
                                lobbyList.append(" " + name + "." + playerAmount + "." + capacity);
                            }
                            this.send(lobbyList.toString());
                            System.out.println(this.server.getIpUsernameMap().get(ip) + "|" + ip
                                               + " requested the lobby list: " + lobbyList + "\r"
                                               + "\n");
                        }
                        catch (RuntimeException exception)
                        {
                            this.send("REQUEST_FAILED");
                            System.out.println("Failed to send lobby list: " + lobbyList + "\r\n");
                            throw new RuntimeException(exception.getMessage());
                        }
                        break;

                    case "JOIN_LOBBY":
                        lobbyName = this.bufferedReader.readLine();
                        this.server.joinLobby(this, lobbyName);
                        break;

                    case "LEAVE_LOBBY":
                        break;

                    case "READY":
                        break;

                    case "CHOOSE_AMOUNT":
                        break;

                    case "COMMUNICATE":
                        break;

                    case "DISCONNECT":
                        isRunning = false;
                        break;

                    default:
                        break;
                }
            }
            catch (ClientDisconnectedException exception)
            {
                String ipAddress = this.clientSocket.getInetAddress().getHostAddress();
                System.err.println(
                    this.server.getIpUsernameMap().get(ip) + "|" + ip + " disconnected." + "\r\n");
                Player player = this.server.getPlayers().get(this);
                this.server.getPlayers().remove(this);
                boolean clientRemoved = false;
                Iterator<Lobby> iterator = this.server.getLobbies().keySet().iterator();
                while (!clientRemoved && iterator.hasNext())
                {
                    Lobby lobby = iterator.next();
                    if (lobby.hasPlayer(player))
                    {
                        lobby.removePlayer(player);
                        clientRemoved = true;
                    }
                }
                isRunning = false;
            }
            catch (RuntimeException | IOException exception)
            {
                System.err.println("Exception: " + exception + "\r\n");
                isRunning = false;
            }

        }
    }

    /**
     * Returns the player object associated with the client.
     *
     * @return a Player object representing the player associated with the client
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Returns the game object associated with the client.
     *
     * @return a Game object representing the game associated with the client
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * Sends a request to the server to start the game.
     */
    public void startGame()
    {
        // sends a request to the server to start the game
    }

    /**
     * Sends a request to the server to end the game.
     */
    public void endGame()
    {
        // sends a request to the server to end the game
    }

    /**
     * Sends a request to the server to update the player's stats based on their performance in
     * the game.
     */
    public void updatePlayerStats()
    {
        // sends a request to the server to update the player's stats based on their performance
        // in the game
    }

    /**
     * Sends a request to the server to disconnect the client from the game.
     */
    public void disconnect()
    {
        // sends a request to the server to disconnect the client from the game
    }
}

