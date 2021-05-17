package educationPractice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOfLifeMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainview.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Game of Life (GoL) KP230");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(975);
        primaryStage.setMinWidth(850);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
