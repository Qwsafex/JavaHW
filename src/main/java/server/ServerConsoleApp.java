package server;


import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ServerConsoleApp {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        @Nullable Thread serverThread = null;

        while (true) {
            System.out.println("cycling");
            String command = scanner.next();
            boolean shutdown = false;
            switch(command) {
                case "start": {
                    String hostname = scanner.next();
                    int port = scanner.nextInt();
                    serverThread = new Thread(() -> {
                        Server server = new Server(Paths.get("."));
                        try {
                            server.run(hostname, port);
                        } catch (IOException e) {
                            System.err.println("Server run failed: " + e.getMessage());
                        }
                    });
                    serverThread.start();
                    break;
                }
                case "stop": {
                    if (serverThread == null) {
                        System.err.println("Server not started!");
                    }
                    else {
                        serverThread.interrupt();
                    }
                    break;
                }
                case "shutdown": {
                    shutdown = true;
                    break;
                }
                default: {
                    System.out.println("Usage: start HOST PORT | stop | shutdown");
                }
            }
            if (shutdown) {
                break;
            }
        }
    }
}
