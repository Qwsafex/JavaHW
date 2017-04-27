package server;

import org.jetbrains.annotations.NotNull;
import utils.SmallReadableMessage;
import utils.WritableMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.Iterator;

class NonBlockingServer implements FTPServer {
    private final Path root;

    NonBlockingServer(Path root) {
        this.root = root;
    }

    @Override
    public void run(@NotNull String hostname, int port) throws IOException {
        FileSystem fileSystem = new FileSystem(root);
        System.out.println("run");
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("binded to " + hostname + ":" + port);
        serverSocketChannel.bind(new InetSocketAddress(hostname, port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        QueryProcessor queryProcessor = new QueryProcessor();
        while (!Thread.interrupted()) {
            selector.selectNow();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    SelectionKey newSelectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    newSelectionKey.attach(new SmallReadableMessage(socketChannel));
                }
                if (selectionKey.isReadable()) {
                    SmallReadableMessage message = (SmallReadableMessage) selectionKey.attachment();
                    if (message.read()) {
                        SelectionKey newSelectionKey = selectionKey.channel().register(selector, SelectionKey.OP_WRITE);
                        newSelectionKey.attach(queryProcessor.process(message.getData(), fileSystem).generateMessage(message.getChannel()));
                    }
                }
                if (selectionKey.isWritable()) {
                    WritableMessage message = (WritableMessage) selectionKey.attachment();
                    if (message.write()) {
                        selectionKey.channel().close();
                        selectionKey.cancel();
                    }
                }
                keyIterator.remove();
            }
        }
    }
}
