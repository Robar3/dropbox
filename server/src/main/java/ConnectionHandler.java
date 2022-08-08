import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private DataInputStream is;
    private DataOutputStream os;

    private Socket socket;


    public ConnectionHandler(Socket socket) throws IOException, InterruptedException {
        System.out.println("Connection accepted");
        this.socket = socket;
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        Thread.sleep(2000);
    }


    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        while (!socket.isClosed()) {
            try {
                String command = is.readUTF();
                if (command.equals("./upload")) {
                    String fileName = is.readUTF();
                    System.out.println("fileName: " + fileName);
                    long fileLength = is.readLong();
                    System.out.println("fileLength: " + fileLength);
                    File file = new File(Server.serverPath + "/" + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        for (long i = 0; i < (fileLength / 1024 == 0 ? 1 : fileLength / 1024); i++) {
                            int bytesRead = is.read(buffer);
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    os.writeUTF("OK");
                } else if (command.equals("./download")) {
                    System.out.println("./download");
                } else if (command.equals("./close")) {
                    closeConnection();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnection() {

        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}