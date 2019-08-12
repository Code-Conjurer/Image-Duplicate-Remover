package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Scene debugScene;//////////////////////////////////////////////////////////////////

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Screen.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        controller.initializeOpenMenuItem(primaryStage);
        controller.initializeGoButton();

        primaryStage.setTitle("Hello World");
        debugScene = new Scene(root, 900, 700);//////////////////////////////////////////////////////////////////
        primaryStage.setScene(debugScene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
        System.out.println(debugScene.getHeight());
        System.out.println(debugScene.getWidth());
    }


}
