import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Dropbox");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                Controller.os.writeUTF("./close");
                Controller.socket.getOutputStream().write("close".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

