package org.example.Client;

import java.io.*;
import java.net.*;
import java.util.Random;

public class TestClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TestClient <port>");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
            return;
        }

        try (Socket socket = new Socket("localhost", port);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server on port " + port);

            Random random = new Random();
            String[] operations = {"ADD", "SUB", "MUL", "DIV", "INVALID"};

            for (int i = 0; i < 30; i++) {
                String operation = operations[random.nextInt(operations.length)];
                int arg1 = random.nextInt(100);
                int arg2 = random.nextInt(100);

                if ("DIV".equals(operation) && arg2 == 0) {
                    arg2 = 1;
                }

                String request = "INVALID".equals(operation)
                        ? "INVALID_REQUEST"
                        : operation + " " + arg1 + " " + arg2;

                System.out.println("Sending request: " + request);
                serverWriter.println(request);

                String response = serverReader.readLine();
                if (response != null) {
                    System.out.println("Server response: " + response);
                } else {
                    System.out.println("No response from server. Connection closed.");
                    break;
                }

                Thread.sleep(500);
            }

        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Client interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

