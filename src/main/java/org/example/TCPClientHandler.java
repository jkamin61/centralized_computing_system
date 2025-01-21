package org.example;

import java.io.*;
import java.net.Socket;

public class TCPClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Statistics statistics;

    public TCPClientHandler(Socket clientSocket, Statistics statistics) {
        this.clientSocket = clientSocket;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Można wyświetlić surowy ciąg polecenia:
                System.out.println("[TCPClientHandler] Received: " + line);

                String[] parts = line.split(" ");
                if (parts.length != 3) {
                    writer.println("ERROR");
                    statistics.incrementInvalidOperations();
                    System.out.println("Operation error: Invalid format (expected 3 parts).");
                    continue;
                }

                String oper = parts[0];
                int arg1, arg2;
                try {
                    arg1 = Integer.parseInt(parts[1]);
                    arg2 = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    writer.println("ERROR");
                    statistics.incrementInvalidOperations();
                    System.out.println("Operation error: Non-integer arguments.");
                    continue;
                }

                // Wyliczamy wynik
                try {
                    int result = performOperation(oper, arg1, arg2);
                    writer.println(result); // wysyłamy do klienta
                    statistics.incrementOperations(oper, result);

                    // Log na konsoli
                    System.out.println("Operation " + oper + " " + arg1 + " " + arg2 + " => " + result);
                } catch (ArithmeticException e) {
                    // Division by zero
                    writer.println("ERROR");
                    statistics.incrementInvalidOperations();
                    System.out.println("Operation error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    // Niepoprawna operacja
                    writer.println("ERROR");
                    statistics.incrementInvalidOperations();
                    System.out.println("Operation error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }

    private int performOperation(String oper, int arg1, int arg2) {
        switch (oper) {
            case "ADD":
                return arg1 + arg2;
            case "SUB":
                return arg1 - arg2;
            case "MUL":
                return arg1 * arg2;
            case "DIV":
                if (arg2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return arg1 / arg2;
            default:
                throw new IllegalArgumentException("Invalid operation: " + oper);
        }
    }
}
