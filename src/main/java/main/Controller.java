package main;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller{

    @FXML
    private Canvas leftCanvas;

    @FXML
    private Canvas rightCanvas;

    @FXML
    private Button goButton;

    @FXML
    private Button skipButton;

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
    private String directoryName, leftImageName, rightImageName;

    //Requires theStage to have been set
    public void initialize(){
        //System.out.println("initializing");
        leftCanvas.widthProperty().bind(leftCanvasParent.widthProperty());
        leftCanvas.heightProperty().bind(leftCanvasParent.heightProperty());
        rightCanvas.widthProperty().bind(rightCanvasParent.widthProperty());
        rightCanvas.heightProperty().bind(rightCanvasParent.heightProperty());

        progressHandler = new ProgressHandler(progressBar);
        canvasHandler = new CanvasHandler(leftCanvas, rightCanvas);
        requestHandler = new RequestHandler(this, progressHandler, canvasHandler, openMenuItem, goButton);
        taskThread = new Thread();  //dummy thread

        directoryName = null;
        leftImageName = null;
        rightImageName = null;
        skipButton.setDisable(true);
    }

    public void initializeOpenMenuItem(final Stage theStage){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                selectedDirectory = directoryChooser.showDialog(theStage);
                if(selectedDirectory != null) {
                    directoryName = selectedDirectory.getName();
                    directoryLabel.setText(directoryName);
                }
            }
        });
    }

    @FXML
    public void goButtonClicked(){
        if (selectedDirectory != null && !taskThread.isAlive()) {
            goButton.setDisable(true);
            openMenuItem.setDisable(true);

            progressHandler.resetProgress();
            Task<Void> mainTask = new MainTask(selectedDirectory, requestHandler);
            taskThread = new Thread(mainTask);
            taskThread.setDaemon(true);
            taskThread.start();
        }
    }

    @FXML
    public void leftCanvasEntered(){
        if(leftImageName != null)
            directoryLabel.setText(leftImageName);
    }

    @FXML
    public void leftCanvasExited(){
        directoryLabel.setText(directoryName);
    }

    @FXML
    public void rightCanvasEntered(){
        if(rightImageName != null)
            directoryLabel.setText(rightImageName);
    }

    @FXML
    public void rightCanvasExited(){
        directoryLabel.setText(directoryName);
    }

    @FXML
    public void leftCanvasClicked(){
        deletionResponse(DeleteResponse.LEFT);
    }

    @FXML
    public void rightCanvasClicked(){
        deletionResponse(DeleteResponse.RIGHT);
    }

    @FXML
    public void skipButtonClicked(){
        deletionResponse(DeleteResponse.SKIP);
    }

    private void deletionResponse(DeleteResponse deleteResponse){
        synchronized (this) {
            if (requestHandler.isWaiting()) {
                requestHandler.setDeleteResponse(deleteResponse);
                skipButton.setDisable(true);
                leftImageName = null;
                rightImageName = null;
                requestHandler.wakeUp();
            }
        }
    }

    //This will run in MainTask thread
    public void signalDeletion(ImageFile imageFileLeft, ImageFile imageFileRight) {
        synchronized (this) {
            skipButton.setDisable(false);
            leftImageName = imageFileLeft.getName();
            rightImageName = imageFileRight.getName();
        }
    }
}
