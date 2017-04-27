package server;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface of a simple FTP server.
 */

public interface FTPServer {
    /**
     * Launches server.
     *
     * After launch it will loop indefinitely until its thread will be interrupted.
     * @param hostname the hostname to listen to
     * @param port the port to listen to
     * @throws IOException If an I/O error occurs
     */
    void run(String hostname, int port) throws IOException;

    /**
     * Creates non-blocking FTP server.
     * @param root path to root directory for server
     * @return A new FTP server
     */
    static FTPServer getNonBlocking(Path root) {
        return new NonBlockingServer(root);
    }
}
