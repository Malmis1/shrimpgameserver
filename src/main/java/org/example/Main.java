package org.example;

/**
 * A launcher class for the server application.
 */
public class Main
{
    /**
     * The main method that launches the server application by starting it.
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }
}