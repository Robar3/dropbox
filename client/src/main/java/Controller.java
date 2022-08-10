import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button send;
    public ListView<String> listView;
    public TextField text;
    private List<File> clientFileList;
    public static Socket socket;
    private DataInputStream is;
    public static DataOutputStream os;

    public void sendCommand(ActionEvent actionEvent) {
        System.out.println("SEND!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: 7/21/2020 init connect to server
        try {
            socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            Thread.sleep(1000);
            clientFileList = new ArrayList<>();
            String clientPath = "./client/src/main/resources/";
            File dir = new File(clientPath);
            if (!dir.exists()) {
                throw new RuntimeException("directory resource not exists on client");
            }
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                clientFileList.add(file);
                listView.getItems().add(file.getName());
            }
            send.setOnAction(a -> {
                byte[] buffer = new byte[1024];
                String downloadCommand = text.getText().split(" ")[0];
                String fileName = text.getText().split(" ")[1];
                if (downloadCommand.equals("./download")) {
                    try {
                        os.writeUTF(text.getText());
                        String exist =is.readUTF();
                        if (exist.equals("Ok")){
                            long fileLength = is.readLong();
                            File file = new File(clientPath + "/" + fileName);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                for (long i = 0; i < (fileLength / 1024 == 0 ? 1 : fileLength / 1024); i++) {
                                    int bytesRead = is.read(buffer);
                                    fos.write(buffer, 0, bytesRead);
                                }
                            }
                            clientFileList.add(file);
                            listView.getItems().add(file.getName());
                        }else {
                            Alert alert =new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Файла на сервере не существует");
                            alert.show();
                        }


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
            listView.setOnMouseClicked(a -> {
                if (a.getClickCount() == 2) {
                    String fileName = listView.getSelectionModel().getSelectedItem();
                    File currentFile = findFileByName(fileName);
                    if (currentFile != null) {
                        try {
                            os.writeUTF("./upload");
                            os.writeUTF(fileName);
                            os.writeLong(currentFile.length());
                            FileInputStream fis = new FileInputStream(currentFile);
                            byte[] buffer = new byte[1024];
                            while (fis.available() > 0) {
                                int bytesRead = fis.read(buffer);
                                os.write(buffer, 0, bytesRead);
                            }
                            os.flush();
                            String response = is.readUTF();
                            System.out.println(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File findFileByName(String fileName) {
        for (File file : clientFileList) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
}