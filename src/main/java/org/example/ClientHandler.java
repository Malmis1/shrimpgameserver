package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.example.logic.Game;
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

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String[] input = this.bufferedReader.readLine().split(" ");
                String lobbyName = null;
                switch (input[0])
                {
                    case "REQUEST_USERNAME":
                        String username = this.server.getUsernameCollection().getRandomUsername();
                        this.bufferedWriter.write("USERNAME " + username + "\r\n");
                        this.bufferedWriter.flush();
                        this.player = new Player(username, 0, 5);
                        this.server.addPlayer(this.player, this);
                        break;

                    case "BECOME_ADMIN":
                        String password = input[1];
                        if (password.equals(this.server.getAdminPassword()))
                        {
                            this.bufferedWriter.write("BECOME_ADMIN_SUCCESSFUL" + "\r\n");
                        }
                        else
                        {
                            this.bufferedWriter.write("BECOME_ADMIN_FAILED" + "\r\n");
                        }
                        this.bufferedWriter.flush();
                        break;

                    case "CREATE_LOBBY":
                        lobbyName = this.bufferedReader.readLine();
                        int numberOfPlayers = Integer.parseInt(this.bufferedReader.readLine());
                        int numberOfRounds = Integer.parseInt(this.bufferedReader.readLine());
                        int roundTime = Integer.parseInt(this.bufferedReader.readLine());
                        int minShrimpPounds = Integer.parseInt(this.bufferedReader.readLine());
                        int maxShrimpPounds = Integer.parseInt(this.bufferedReader.readLine());
                        this.server.createLobby(lobbyName, numberOfPlayers, numberOfRounds,
                                                roundTime, minShrimpPounds, maxShrimpPounds);
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
                        break;

                    default:
                        break;
                }
            }


        }
        catch (IOException exception)
        {
            System.err.println("Exception: " + exception);
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
     * Sends a message to the client.
     *
     * @param message a String representing the message to be sent
     */
    public void sendMessage(String message)
    {
        try
        {
            this.bufferedWriter.write(message + "\r\n");
        }
        catch (IOException exception)
        {
            System.err.println("Failed to send message: " + exception);
        }

    }

    /**
     * Receives a message from the server and returns it as a String.
     *
     * @return a String representing the message received from the server
     */
    public String receiveMessage()
    {
        // receives a message from the server and returns it as a String
        return null;
    }

    /**
     * Sends a request to the server to disconnect the client from the game.
     */
    public void disconnect()
    {
        // sends a request to the server to disconnect the client from the game
    }
}

