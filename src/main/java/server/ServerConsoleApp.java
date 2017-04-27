package server;


import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerConsoleApp {
    public static void main(String[] args) throws IOException {
        DataInputStream inputStream = new DataInputStream(System.in);
        @Nullable
        Thread serverThread = null;
        while (true) {
            if (inputStream.available() > 0) {
                String command = inputStream.readUTF();
                switch(command) {
                    case "start": {
                        int port = inputStream.readInt();
                        serverThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Server server = new Server();
                                try {
                                    server.run(port);
                                } catch (IOException e) {
                                    System.err.println("Server run failed: " + e.getMessage());
                                }
                            }
                        });
                        serverThread.start();
                    }
                    case "stop": {
                        if (serverThread == null) {
                            System.err.println("Server not started!");
                        }
                        else {
                            serverThread.interrupt();
                        }
                    }
                }
            }
        }
    }
}
