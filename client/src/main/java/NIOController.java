import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ResourceBundle;

public class NIOController implements Initializable {

    public Button send;
    public ListView<String> listView;
    public TextField text;
    Selector selector;

    private String clientPath = "./client/src/main/resources/";

    public void sendCommand(ActionEvent actionEvent) {


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8189);
        try {
            SocketChannel socketChannel = SocketChannel.open(serverAddress);
            socketChannel.configureBlocking(false);
            send.setOnAction(a -> {
                ByteBuffer buffer = ByteBuffer.allocate(256);
                String splitText[] = text.getText().split(" ");
                buffer.put(text.getText().getBytes());
                try {
                    buffer.flip();
                    socketChannel.write(buffer);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (splitText.length>1){
                    String downloadCommand = splitText[0];
                    String fileName = splitText[1];

                    try (FileChannel fileChannel = FileChannel.open(Paths.get(clientPath + fileName))) {
                        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            ByteBuffer buf =ByteBuffer.allocate(256);
                    try {
                        socketChannel.read(buf);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    buf.flip();
                    StringBuilder msg = new StringBuilder();
                    while (buf.hasRemaining()) {
                        msg.append((char) buf.get());
                    }
                    System.out.println(msg);

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
}
