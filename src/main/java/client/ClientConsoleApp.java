package client;

import server.Server;
import utils.SimpleFile;

import java.io.IOException;

public class ClientConsoleApp {
    public static void main(String[] args) throws IOException {
        String hostname = args[1];
        /*int port = Integer.valueOf(args[2]);
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            try {
                server.run(hostname, port);
            } catch (IOException e) {
                System.err.println("Server run failed: " + e.getMessage());
            }
        });
        serverThread.start();
        */
        Client client = new Client();
        System.out.println("heh");
        client.connect(args[1], Integer.valueOf(args[2]));
        System.out.println("meh");
        switch (args[0]) {
            case "get": {
                String filename = client.executeGet(args[3]);
                System.out.println("Downloaded file to " + filename);
                break;
            }
            case "list": {
                for (SimpleFile file : client.executeList(args[3])) {
                    System.out.println(file.name + " " + file.isDirectory);
                }
                break;
            }
            default: {
                System.out.println("Usage is: get PATH or list PATH");
            }

        }
        client.disconnect();
    }
}
