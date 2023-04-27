package org.example;

/**
 * Represents an exception for the client being disconnected.
 */
public class ClientDisconnectedException extends RuntimeException
{
    /**
     * Constructs a new runtime exception with a default message.
     */
    public ClientDisconnectedException()
    {
        super("Client has disconnected");
    }
}
