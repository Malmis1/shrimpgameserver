package org.example;

public class ClientDisconnectedException extends RuntimeException
{
    public ClientDisconnectedException()
    {
        super("Client has disconnected");
    }
}
