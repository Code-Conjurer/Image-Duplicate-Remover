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

    //Requires theStage to have been set
    public void initialize(){
        //System.out.println("initializing");
        leftCanvas.widthProperty().bind(leftCanvasParent.widthProperty());
        leftCanvas.heightProperty().bind(leftCanvasParent.heightProperty());
        rightCanvas.widthProperty().bind(rightCanvasParent.widthProperty());
        rightCanvas.heightProperty().bind(rightCanvasParent.heightProperty());

        progressHandler = new ProgressHandler(progressBar);
        canvasHandler = new CanvasHandler(leftCanvas, rightCanvas);
        requestHandler = new RequestHandler(this, progressHandler, canvasHandler);
        taskThread = new Thread();  //dummy thread

        skipButton.setDisable(true);
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

    @FXML
    public void goButtonClicked(){
        goButton.setDisable(true);
        if (selectedDirectory != null && !taskThread.isAlive()) {
            progressHandler.resetProgress();
            Task<Void> mainTask = new MainTask(selectedDirectory, requestHandler);
            taskThread = new Thread(mainTask);
            taskThread.setDaemon(true);
            taskThread.start();
        }
        goButton.setDisable(false);
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
                canvasHandler.hideLeftToolTip();
                canvasHandler.hideRightToolTip();
                requestHandler.wakeUp();
            }
        }
    }

    //This will run in MainTask thread
    public void requestDeletion(ImageFile imageFileLeft, ImageFile imageFileRight) {
        synchronized (this) {
            skipButton.setDisable(false);
            canvasHandler.setLeftToolTip(imageFileLeft);
            canvasHandler.setRightToolTip(imageFileRight);
            canvasHandler.showLeftToolTip();
            canvasHandler.showRightToolTip();
        }
    }
}
