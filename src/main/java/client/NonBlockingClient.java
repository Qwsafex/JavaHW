package client;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import utils.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.List;

/**
 * Class that implements non-blocking FTP client.
 */

public class NonBlockingClient implements Client{

    private final SocketChannel channel;

    NonBlockingClient() throws IOException {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
    }

    @Override
    public void connect(@NotNull String hostname, int port) throws IOException {
        System.out.println("connecting to " + hostname + ":" + port);
        if (channel.isConnected()) return;
        channel.connect(new InetSocketAddress(hostname, port));

        //noinspection StatementWithEmptyBody
        while(!channel.finishConnect()) {
            //System.out.println("not connected");
        }
    }
    @Override
    public void disconnect() throws IOException {
        channel.close();
    }

    @NotNull
    @Override
    public List<SimpleFile> executeList(@NotNull String path) throws IOException {
        sendRequest(createSentData((byte) Query.LIST.ordinal(), path.getBytes()));
        byte[] response = getSmallResponse();
        ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(response));
        try {
            //noinspection unchecked
            return (List<SimpleFile>) objectStream.readObject();
        } catch (ClassNotFoundException e) {
            // TODO: probably change to custom exception
            throw new RuntimeException("server.NonBlockingServer fucked up!");
        }
    }


    @NotNull
    @Override
    public Path executeGet(@NotNull String path) throws IOException {
        sendRequest(createSentData((byte) Query.GET.ordinal(), path.getBytes()));
        return getBigResponse().getFilename();
    }
    private byte[] getSmallResponse() throws IOException {
        SmallReadableMessage message = new SmallReadableMessage(channel);
        getResponse(message);
        return message.getData();
    }

    private BigReadableMessage getBigResponse() throws IOException {
        BigReadableMessage message = new BigReadableMessage(channel);
        getResponse(message);
        return message;
    }

    private void getResponse(ReadableMessage message) throws IOException {
        //noinspection StatementWithEmptyBody
        while (!message.read());
    }

    private void sendRequest(byte[] data) throws IOException {
        //noinspection StatementWithEmptyBody
        if (!channel.isConnected()) {
            throw new NotYetConnectedException();
        }
        WritableMessage message = new SmallWritableMessage(channel, data);
        //noinspection StatementWithEmptyBody
        while (!message.write());
    }

    private byte[] createSentData(byte first, byte[] rest) {
        return ArrayUtils.addAll(ByteUtils.byteToBytes(first), rest);
    }

}
