package server;

import utils.SmallReadableMessage;
import utils.SmallWritableMessage;
import utils.WritableMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    public void run(String hostname, int port) throws IOException {
        System.out.println("run");
        Selector selector = Selector.open();
        System.out.println("1");
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("2");
        System.out.println("binded to " + hostname + ":" + port);
        serverSocketChannel.bind(new InetSocketAddress(hostname, port));
        System.out.println("3");
        serverSocketChannel.configureBlocking(false);
        System.out.println("4");
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("5");
        QueryProcessor queryProcessor = new QueryProcessor();
        System.out.println("6");
        while (true) {
            //System.out.println("hey");
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
                        selectionKey.cancel();
                        SelectionKey newSelectionKey = selectionKey.channel().register(selector, SelectionKey.OP_WRITE);
                        newSelectionKey.attach(new SmallWritableMessage(message.getChannel(), queryProcessor.process(message.getData())));
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
