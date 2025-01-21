# Centralized Computing System (CCS)

**Centralized Computing System (CCS)** is a server application written in Java, designed to perform the functions of a centralized computing system. This project was developed as part of the **System Communication and Networking (SKJ)** course at PJATK.

---

## Features

### 1. **Service Discovery (UDP)**
- The server listens on a UDP port specified as a command-line argument during startup.
- Upon receiving the message **`CCS DISCOVER`**, the server responds with **`CCS FOUND`**.
- This mechanism allows clients to discover the server in a local network using broadcast packets.

### 2. **Client Communication (TCP)**
- The server opens a TCP port (the same as the UDP port) and handles multiple clients simultaneously.
- The client sends mathematical operation requests in the following format:

```<OPER> <ARG1> <ARG2>```
where:
- `OPER` is one of the operations: `ADD`, `SUB`, `MUL`, `DIV`.
- `ARG1` and `ARG2` are integers.
- The server responds with the result of the operation or **`ERROR`** in case of an invalid request.

### 3. **Statistics Reporting**
- The server collects both global and 10-second interval statistics, including:
- The number of connected clients.
- The number of completed operations.
- The count of each type of operation (`ADD`, `SUB`, `MUL`, `DIV`).
- The number of invalid operations.
- The sum of all operation results.
- Statistics are displayed in the server console every 10 seconds.

---

## Requirements

- **Java 1.8** (as per project requirements).
- A client application for testing (a dedicated client is included in the repository).

---

## Manual Compilation

If you want to compile the project manually:

Navigate to the `src/main/java/` directory.
Compile the `.java` files:
```bash
  javac -d target/classes src/main/java/org/example/*.java
```
Create the JAR file:
```bash
  jar cfm centralized_computing_system.jar target/classes/META-INF/MANIFEST.MF -C target/classes
```
## Running the Server

To start the server, execute the JAR file:
```bash
  java -jar centralized_computing_system.jar <port>
```
Replace <port> with the desired UDP/TCP port number (e.g., 8000).

## Client Application

The client application sends requests to the server and receives responses. It operates in two stages:

1. Server Discovery (UDP)

    - The client broadcasts a CCS DISCOVER message to the local network.
    - Upon receiving the CCS FOUND response, it retrieves the server's IP address.
2. Communication with the Server (TCP)

    - The client connects to the server using the discovered IP address and the same port.
    - It sends random mathematical operations and receives responses.
### Communication Example
Client Request:
        `ADD 10 20`

Server Response:
        `30`

In case of an error:
`ERROR`
