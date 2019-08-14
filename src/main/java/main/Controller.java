package main;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private Canvas leftCanvas;

    @FXML
    private Canvas rightCanvas;

    @FXML
    private Button goButton;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label directoryLabel;

    private File selectedDirectory = null;
    private ProgressHandler progressHandler;
    private CanvasHandler canvasHandler;

    //Requires theStage to have been set
    public void initialize(){
        System.out.println("initializing");
        progressHandler = new ProgressHandler(this);
        canvasHandler = new CanvasHandler(this);

    }

    public Canvas getLeftCanvas(){
        return leftCanvas;
    }

    public Canvas getRightCanvas(){
        return rightCanvas;
    }

    public ProgressBar getProgressBar(){
        return progressBar;
    }

    public void initializeOpenMenuItem(final Stage theStage){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                selectedDirectory = directoryChooser.showDialog(theStage);
                if(selectedDirectory != null)
                    directoryLabel.setText(selectedDirectory.toURI().toString());
            }
        });
    }

    public void initializeGoButton(){
        final Controller controller = this;

        goButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (selectedDirectory != null) {
                    progressHandler.resetProgress();
                    Task<Void> mainTask = new MainTask(selectedDirectory, progressHandler, canvasHandler);
                    Thread th = new Thread(mainTask);
                    th.setDaemon(true);
                    th.start();

                }
            }
        });
    }

}
