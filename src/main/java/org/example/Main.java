package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar CCS.jar <port>");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            CCS server = new CCS(port);
            server.start();
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }
}
