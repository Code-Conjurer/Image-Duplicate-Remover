package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private Canvas leftCanvas, rightCanvas;

    @FXML
    private Button goButton;

    @FXML
    private MenuItem openMenuItem;

    File selectedDirectory = null;

    //Requires theStage to have been set
    public void initialize(){
        System.out.println("initializing");
        
    }

    public void initializeOpenMenuItem(Stage theStage){
        DirectoryChooser directoryChooser = new DirectoryChooser();

        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedDirectory = directoryChooser.showDialog(theStage);
            }
        });
    }

    public void initializeGoButton(){

        goButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (selectedDirectory != null) {

                }
            }
        });
    }

}
