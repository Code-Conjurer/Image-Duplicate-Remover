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
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @FXML
    private AnchorPane leftCanvasParent;

    @FXML
    private AnchorPane rightCanvasParent;


    private File selectedDirectory = null;
    private ProgressHandler progressHandler;
    private CanvasHandler canvasHandler;
    private RequestHandler requestHandler;
    private Thread taskThread;

    //Requires theStage to have been set
    public void initialize(){
        System.out.println("initializing");
        leftCanvas.widthProperty().bind(leftCanvasParent.widthProperty());
        leftCanvas.heightProperty().bind(leftCanvasParent.heightProperty());
        rightCanvas.widthProperty().bind(rightCanvasParent.widthProperty());
        rightCanvas.heightProperty().bind(rightCanvasParent.heightProperty());

        progressHandler = new ProgressHandler(this);
        canvasHandler = new CanvasHandler(this);
        requestHandler = new RequestHandler(this);
        taskThread = new Thread();  //dummy thread

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
        goButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                goButton.setDisable(true);
                if (selectedDirectory != null && !taskThread.isAlive()) {
                    progressHandler.resetProgress();
                    Task<Void> mainTask = new MainTask(selectedDirectory, progressHandler, canvasHandler, requestHandler);
                    taskThread = new Thread(mainTask);
                    taskThread.setDaemon(true);
                    taskThread.start();
                }
                goButton.setDisable(false);
            }
        });
    }

    public void response(){
        synchronized (this){
            notify();
        }
    }

}
