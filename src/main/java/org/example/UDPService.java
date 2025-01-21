package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPService implements Runnable {
    private final int port;
    private final Statistics statistics;

    public UDPService(int port, Statistics statistics) {
        this.port = port;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        try (DatagramSocket udpSocket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];
            System.out.println("UDP Service listening on port: " + port);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                if (received.startsWith("CCS DISCOVER")) {
                    String response = "CCS FOUND";
                    DatagramPacket responsePacket = new DatagramPacket(
                            response.getBytes(),
                            response.length(),
                            packet.getAddress(),
                            packet.getPort()
                    );
                    udpSocket.send(responsePacket);
                }
            }
        } catch (IOException e) {
            System.out.println("UDP Service error: " + e.getMessage());
        }
    }
}
