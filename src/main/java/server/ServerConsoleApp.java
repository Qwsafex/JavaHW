package server;


import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ServerConsoleApp {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        @Nullable
        Thread serverThread = null;
        while (true) {
            System.out.println("cycling");
            String command = scanner.next();
            switch(command) {
                case "start": {
                    //String hostname = scanner.next();
                    //int port = scanner.nextInt();
                    String hostname = "localhost";
                    int port = 1234;
                    serverThread = new Thread(() -> {
                        Server server = new Server();
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
            }
        }
    }
}
