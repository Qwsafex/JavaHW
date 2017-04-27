package client;

import utils.SimpleFile;

import java.io.IOException;

public class ClientConsoleApp {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect(args[1], Integer.valueOf(args[2]));
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
