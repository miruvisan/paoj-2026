package com.pao.laboratory13.exercise2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int PORT = 9000;

    private static final AtomicInteger activeClients = new AtomicInteger(0);

    enum State {
        INIT, AUTH, OPEN, CLOSED
    }
    public static void main(String[] args) {
        System.out.println("=== START DEMO ===");

        Thread serverThread = new Thread(() -> runServer());
        serverThread.start();

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        Thread client1 = new Thread(() -> runClient1());
        Thread client2 = new Thread(() -> runClient2());

        client1.start();
        client2.start();

        try {
            client1.join();
            client2.join();
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== DEMO DONE! ===");
    }

    private static void runServer() {
        System.out.println("[SERVER] Listening on port " + PORT + "...");
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(1500);

            while (activeClients.get() == 0 || activeClients.get() > 0) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    int clientId = activeClients.incrementAndGet();
                    System.out.println("[SERVER] [CLIENT-" + clientId + "] Connected");

                    pool.execute(new ClientSessionHandler(clientSocket, clientId));
                } catch (SocketTimeoutException e) {
                    if (activeClients.get() == 0) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Eroare la pornire: " + e.getMessage());
        } finally {
            System.out.println("[SERVER] All clients done. Shutting down pool...");
            pool.shutdown();
            try {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException ignored) {}
            System.out.println("[SERVER] Stopped.");
        }
    }

    private static class ClientSessionHandler implements Runnable {
        private final Socket socket;
        private final int clientId;

        public ClientSessionHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            State currentState = State.INIT;
            String currentUser = "";
            int historyCount = 0;

            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    System.out.println("[CLIENT-" + clientId + "] >> " + line);
                    String[] tokens = line.split("\\s+");
                    String command = tokens[0];

                    if (currentState == State.CLOSED) {
                        writer.println("ERR E_STATE CLOSED");
                        continue;
                    }

                    switch (command) {
                        case "AUTH":
                            if (tokens.length != 2) {
                                writer.println("ERR E_PARSE AUTH");
                            } else {
                                currentUser = tokens[1];
                                currentState = State.AUTH;
                                historyCount = 0;
                                writer.println("OK AUTH user=" + currentUser);
                            }
                            break;
                        case "OPEN":
                            if (tokens.length != 1) {
                                writer.println("ERR E_PARSE OPEN");
                            } else if (currentState == State.OPEN) {
                                writer.println("ERR E_STATE ALREADY_OPEN");
                            } else if (currentState == State.INIT) {
                                writer.println("ERR E_STATE NOT_OPEN");
                            } else {
                                currentState = State.OPEN;
                                writer.println("OK OPEN");
                            }
                            break;
                        case "SEND":
                            if (tokens.length < 2) {
                                writer.println("ERR E_PARSE SEND");
                            } else if (currentState != State.OPEN) {
                                writer.println("ERR E_STATE NOT_OPEN");
                            } else {
                                historyCount++;
                                writer.println("OK OPEN sent");
                            }
                            break;
                        case "BROADCAST":
                            if (tokens.length < 2) {
                                writer.println("ERR E_PARSE BROADCAST");
                            } else if (currentState != State.OPEN) {
                                writer.println("ERR E_STATE NOT_OPEN");
                            } else {
                                historyCount++;
                                writer.println("OK OPEN broadcast");
                            }
                            break;
                        case "HISTORY":
                            if (tokens.length != 1) {
                                writer.println("ERR E_PARSE HISTORY");
                            } else if (currentState != State.OPEN) {
                                writer.println("ERR E_STATE NOT_OPEN");
                            } else {
                                writer.println("OK OPEN history=" + historyCount);
                            }
                            break;
                        case "CLOSE":
                            if (tokens.length != 1) {
                                writer.println("ERR E_PARSE CLOSE");
                            } else if (currentState != State.OPEN) {
                                writer.println("ERR E_STATE NOT_OPEN");
                            } else {
                                currentState = State.CLOSED;
                                writer.println("OK CLOSED");
                            }
                            break;
                        default:
                            writer.println("ERR E_PARSE UNKNOWN_COMMAND");
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("[SERVER] Eroare în sesiunea clientului " + clientId + ": " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                System.out.println("[CLIENT-" + clientId + "] Disconnected");
                activeClients.decrementAndGet();
            }
        }
    }

    private static void runClient1() {
        try (
                Socket socket = new Socket("localhost", PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("AUTH alice");
            System.out.println("[CLIENT-1] << " + in.readLine());
            Thread.sleep(200);

            out.println("OPEN");
            System.out.println("[CLIENT-1] << " + in.readLine());
            Thread.sleep(200);

            out.println("SEND hi");
            System.out.println("[CLIENT-1] << " + in.readLine());
            Thread.sleep(200);

            out.println("CLOSE");
            System.out.println("[CLIENT-1] << " + in.readLine());
        } catch (Exception e) {
            System.err.println("[CLIENT-1] Eroare: " + e.getMessage());
        }
    }

    private static void runClient2() {
        try (
                Socket socket = new Socket("localhost", PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("AUTH bob");
            System.out.println("[CLIENT-2] << " + in.readLine());
            Thread.sleep(300);

            out.println("OPEN");
            System.out.println("[CLIENT-2] << " + in.readLine());
            Thread.sleep(100);

            out.println("BROADCAST x");
            System.out.println("[CLIENT-2] << " + in.readLine());
            Thread.sleep(200);

            out.println("HISTORY");
            System.out.println("[CLIENT-2] << " + in.readLine());
        } catch (Exception e) {
            System.err.println("[CLIENT-2] Eroare: " + e.getMessage());
        }
    }
}
