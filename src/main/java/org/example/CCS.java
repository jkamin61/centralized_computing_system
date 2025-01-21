package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CCS {
    private final int port;
    private final Statistics statistics;
    private final ExecutorService clientHandlerPool;

    public CCS(int port) {
        this.port = port;
        this.statistics = new Statistics();
        this.clientHandlerPool = Executors.newCachedThreadPool();
    }

    public void start() throws IOException {
        System.out.println("Starting CCS on port: " + port);

        // === URUCHAMIAMY USŁUGĘ UDP W OSOBNYM WĄTKU ===
        Thread udpThread = new Thread(new UDPService(port, statistics));
        udpThread.setDaemon(true);
        udpThread.start();

        long lastReportTime = System.currentTimeMillis();

        try (ServerSocket tcpServerSocket = new ServerSocket(port)) {
            System.out.println("TCP Server listening on port: " + port);

            while (true) {
                long currentTime = System.currentTimeMillis();

                // Co 10 sekund wypisujemy statystyki
                if (currentTime - lastReportTime >= 10000) {
                    synchronized (statistics) {
                        statistics.printStatistics();
                        statistics.resetRecentStatistics();
                    }
                    lastReportTime = currentTime;
                }

                // Akceptujemy połączenie (z timeoutem 1s)
                tcpServerSocket.setSoTimeout(1000);
                try {
                    Socket clientSocket = tcpServerSocket.accept();
                    statistics.incrementClients();
                    clientHandlerPool.execute(new TCPClientHandler(clientSocket, statistics));
                } catch (IOException ignored) {
                    // Timeout mija, wracamy do pętli, by ewentualnie wyświetlić statystyki
                }
            }
        }
    }
}
