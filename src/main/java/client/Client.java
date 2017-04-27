package client;

import org.jetbrains.annotations.NotNull;
import utils.SimpleFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface of a simple FTP client.
 */

public interface Client {
    /**
     * Enum of all possible client queries.
     */
    enum Query {
        /**
         * Query to list all files in directory
         */
        LIST,
        /**
         * Query to download file.
         */
        GET
    }

    /**
     * Tries to establish connection with server hostname:port.
     * @param hostname the hostname of server
     * @param port the port number
     * @throws IOException If some I/O error during connection occurs
     */
    void connect(@NotNull String hostname, int port) throws IOException;

    /**
     * Closes connection with server.
     * @throws IOException If an I/O error during connection close occurs
     */
    void disconnect() throws IOException;

    /**
     * Return list of files located in the specified directory.
     * @param path The path to the directory
     * @return List with all files in the directory.
     * @throws IOException If an I/O error occurs
     */
    @NotNull
    List<SimpleFile> executeList(@NotNull String path) throws IOException;

    /**
     * Downloads file.
     * @param path path to file on server
     * @return path to downloaded file on client
     * @throws IOException If an I/O error occurs
     */
    @NotNull
    Path executeGet(@NotNull String path) throws IOException ;

    /**
     * Creates non-blocking FTP client.
     * @return A new FTP client
     * @throws IOException If an I/O error occurs
     */
    @NotNull
    static Client getNonBlocking() throws IOException {
        return new NonBlockingClient();
    }
}
