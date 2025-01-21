package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private final AtomicInteger totalClients = new AtomicInteger(0);
    private final AtomicInteger totalOperations = new AtomicInteger(0);
    private final AtomicInteger invalidOperations = new AtomicInteger(0);
    private final AtomicInteger resultSum = new AtomicInteger(0);
    private final Map<String, AtomicInteger> operationCounts = new ConcurrentHashMap<>();

    private final AtomicInteger recentOperations = new AtomicInteger(0);
    private final AtomicInteger recentInvalidOperations = new AtomicInteger(0);
    private final AtomicInteger recentResultSum = new AtomicInteger(0);
    private final Map<String, AtomicInteger> recentOperationCounts = new ConcurrentHashMap<>();

    public Statistics() {
        for (String operation : new String[]{"ADD", "SUB", "MUL", "DIV"}) {
            operationCounts.put(operation, new AtomicInteger(0));
            recentOperationCounts.put(operation, new AtomicInteger(0));
        }
    }

    public void incrementClients() {
        totalClients.incrementAndGet();
    }

    public void incrementOperations(String operation, int result) {
        totalOperations.incrementAndGet();
        resultSum.addAndGet(result);
        operationCounts.get(operation).incrementAndGet();

        recentOperations.incrementAndGet();
        recentResultSum.addAndGet(result);
        recentOperationCounts.get(operation).incrementAndGet();
    }

    public void incrementInvalidOperations() {
        invalidOperations.incrementAndGet();
        recentInvalidOperations.incrementAndGet();
    }

    public synchronized void printStatistics() {
        System.out.println("=== Global Statistics ===");
        System.out.println("Total clients connected: " + totalClients.get());
        System.out.println("Total operations performed: " + totalOperations.get());
        System.out.println("Invalid operations: " + invalidOperations.get());
        System.out.println("Result sum: " + resultSum.get());
        operationCounts.forEach((operation, count) ->
                System.out.println("Operation " + operation + ": " + count.get()));

        System.out.println("\n=== Last 10 Seconds Statistics ===");
        System.out.println("Operations performed: " + recentOperations.get());
        System.out.println("Invalid operations: " + recentInvalidOperations.get());
        System.out.println("Result sum: " + recentResultSum.get());
        recentOperationCounts.forEach((operation, count) ->
                System.out.println("Operation " + operation + ": " + count.get()));
    }

    public synchronized void resetRecentStatistics() {
        recentOperations.set(0);
        recentInvalidOperations.set(0);
        recentResultSum.set(0);
        recentOperationCounts.forEach((operation, count) -> count.set(0));
    }
}
