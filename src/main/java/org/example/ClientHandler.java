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
import org.example.logic.GameCollection;
import org.example.logic.Lobby;
import org.example.logic.Player;

/**
 * The ClientHandler class represents a handler for communication between the server and a client.
 * <p>
 * It is responsible for sending and receiving data, processing requests, and handling errors.
 */
public class ClientHandler implements Runnable {
  private final Socket clientSocket;
  private final Server server;
  private BufferedWriter bufferedWriter;
  private BufferedReader bufferedReader;
  private GameCollection gameCollection;
  private Player player;
  private Lobby lobbyJoined;

  /**
   * Constructor for the ClientHandler class.
   *
   * @param socket a Socket object representing the socket used to communicate with the client
   */
  public ClientHandler(Socket socket, Server server) {
    this.clientSocket = socket;
    this.server = server;
    try {
      this.bufferedWriter = new BufferedWriter(
          new OutputStreamWriter(this.clientSocket.getOutputStream()));
      this.bufferedReader = new BufferedReader(
          new InputStreamReader(this.clientSocket.getInputStream()));
    }
    catch (IOException exception) {
      System.err.println("Failed to open stream: " + exception);
    }
  }

  /**
   * Sends a message to the client through the established connection.
   *
   * @param message the message to be sent
   * @throws RuntimeException if there is a failure to send the message to the client
   */
  public void send(String message) {
    try {
      bufferedWriter.write(message + "\r\n");
      bufferedWriter.flush();
    }
    catch (IOException exception) {
      throw new RuntimeException("Failed to send message to the client.");
    }
  }


  /**
   * Receives a message from the client through the established connection.
   *
   * @return the message received from the client
   * @throws RuntimeException if there is a failure to receive the message from the client
   */
  public String receive() {
    try {
      return this.bufferedReader.readLine();
    }
    catch (SocketException exception) {
      throw new ClientDisconnectedException();
    }
    catch (IOException exception) {
      throw new RuntimeException("Failed to receive message from the client.");
    }
  }

  /**
   * Returns the player object associated with the client.
   *
   * @return a Player object representing the player associated with the client
   */
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public void run() {
    boolean isRunning = true;
    while (isRunning) {
      String ip = this.clientSocket.getInetAddress().getHostAddress();
      Map<String, String> ipUsernameMap = this.server.getIpUsernameMap();
      boolean isAdmin = this.server.getIpAdminMap().containsKey(ip);
      try {
        String lobbyName;
        String[] input = this.receive().split(" ");
        switch (input[0]) {
          case "REQUEST_USERNAME":

            if (ipUsernameMap.containsKey(ip)) {
              this.send("USERNAME " + ipUsernameMap.get(ip) + " " + isAdmin);
              this.player = new Player(ipUsernameMap.get(ip), this, 5);
              this.server.getClients().add(this);
              System.out.println(
                  this.server.getIpUsernameMap().get(ip) + "|" + ip + " reconnected." + "\r\n");
            }
            else if (input[1].equals(Server.VERSION)) {
              String username = this.server.getUsernameCollection().getRandomUsername();
              ipUsernameMap.put(ip, username);
              this.send("USERNAME " + username + " " + isAdmin);
              this.player = new Player(username, this, 5);
              this.server.getClients().add(this);
              System.out.println("Gave new client " + ip + " the username: " + username + "\r\n");
            }
            else {
              isRunning = false;
              System.out.println(ip + " tried to connect, but has an older version." + "\r\n");
            }
            break;

          case "BECOME_ADMIN":
            String password = input[1];
            if (password.equals(this.server.getAdminPassword())) {
              this.send("BECOME_ADMIN_SUCCESSFUL");
              this.server.getIpAdminMap().put(ip, true);
              System.out.println(this.server.getIpUsernameMap().get(ip) + "|" + ip
                                 + " entered the correct admin password and became "
                                 + "administrator." + "\r\n");
            }
            else {
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
            String communicationRounds = input[5];
            int communicationRoundTime;
            int minShrimpKilograms;
            int maxShrimpKilograms;
            try {
              numberOfPlayers = Integer.parseInt(input[2]);
              numberOfRounds = Integer.parseInt(input[3]);
              roundTime = Integer.parseInt(input[4]);
              communicationRoundTime = Integer.parseInt(input[6]);
              minShrimpKilograms = Integer.parseInt(input[7]);
              maxShrimpKilograms = Integer.parseInt(input[8]);
              this.server.createLobby(lobbyName, numberOfPlayers, numberOfRounds, roundTime,
                                      communicationRounds, communicationRoundTime, minShrimpKilograms, maxShrimpKilograms);
              this.send("CREATE_LOBBY_SUCCESS");
              this.server.sendLobbyInfoToClients();
              }
            catch (NumberFormatException exception) {
              this.send("CREATE_LOBBY_FAILED");
              throw new RuntimeException(
                  "Invalid user input received when trying to create lobby.");
            }
            catch (RuntimeException exception) {
              this.send("CREATE_LOBBY_FAILED");
              throw new RuntimeException(exception.getMessage());
            }
            break;

          case "REQUEST_LOBBY_LIST":
            StringBuilder lobbyList = new StringBuilder("LOBBY_LIST");
            try {
              for (Lobby lobby : this.server.getLobbyGameSettingsMap().keySet()) {
                String name = lobby.getName();
                String playerAmount = "" + lobby.getPlayers().size();
                String capacity = "" + lobby.getMaxPlayers();
                lobbyList.append(" " + name + "." + playerAmount + "." + capacity);
              }
              this.send(lobbyList.toString());
              System.out.println(
                  this.server.getIpUsernameMap().get(ip) + "|" + ip + " requested the lobby list: "
                  + lobbyList + "\r" + "\n");
            }
            catch (RuntimeException exception) {
              this.send("REQUEST_FAILED");
              throw new RuntimeException(exception.getMessage());
            }
            break;

          case "JOIN_LOBBY":
            synchronized (this.server) {
              lobbyName = input[1];
              Lobby lobby = this.server.getNameLobbyMap().get(lobbyName);
              if (lobby == null) {
                this.send("LOBBY_NOT_EXIST");
              }
              else if (this.server.getLobbyGameSettingsMap().keySet().contains(lobby)) {
                if (!lobby.isFull()) {
                  this.server.joinLobby(this, lobbyName);
                  this.send("LOBBY_JOINED");
                  this.lobbyJoined = lobby;
                  this.server.sendLobbyInfoToClients();
                }
                else {
                  this.send("LOBBY_FULL");
                }
                if (lobby.isFull()) {
                  this.server.startGame(lobby);
                }
              }
            }
            break;

          case "LEAVE_LOBBY":
            this.server.leaveLobby(this);
            this.server.sendLobbyInfoToClients();
            break;

          case "CATCH_SHRIMP":
            int shrimpToCatch = Integer.parseInt(input[1]);
            this.server.catchShrimp(this, shrimpToCatch);
            this.send("CAUGHT_SUCCESSFULLY");
            break;

          case "CHAT_MESSAGE":
            String message = input[1];
            this.server.addMessageToChat(this, message);
            this.send("MESSAGE_RECEIVED");
            break;

          default:
            break;
        }
      }
      catch (ClientDisconnectedException exception) {
        System.err.println(
            this.server.getIpUsernameMap().get(ip) + "|" + ip + " disconnected." + "\r\n");
        this.server.getClients().remove(this);
        boolean clientRemovedFromLobby = false;
        Iterator<Lobby> iterator = this.server.getLobbyGameSettingsMap().keySet().iterator();
        while (!clientRemovedFromLobby && iterator.hasNext()) {
          Lobby lobby = iterator.next();
          if (lobby.hasPlayer(this.player)) {
            lobby.getPlayers().remove(this.player);
            clientRemovedFromLobby = true;
            this.server.sendLobbyInfoToClients();
          }
        }
        isRunning = false;
      }
      catch (RuntimeException exception) {
        System.err.println("Exception: " + exception + "\r\n");
        isRunning = false;
      }

    }
  }


}

