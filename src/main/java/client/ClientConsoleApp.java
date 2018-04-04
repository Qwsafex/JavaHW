package client;

import utils.SimpleFile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Console application for communication with FTP server.
 */

public class ClientConsoleApp {
    /**
     * Main method.
     * @param args command line args
     * @throws IOException If an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        Client client = Client.getNonBlocking();
        System.out.println("heh");
        client.connect(args[1], Integer.valueOf(args[2]));
        System.out.println("meh");
        switch (args[0]) {
            case "get": {
                Path path = client.executeGet(args[3]);
                System.out.println("Downloaded file to " + path);
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
