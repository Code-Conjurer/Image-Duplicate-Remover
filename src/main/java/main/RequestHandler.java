package main;


import javafx.application.Platform;
import javafx.scene.image.Image;

enum DeleteResponse{
    LEFT, RIGHT, SKIP
        }

public class RequestHandler{

    private Controller controller;
    private DeleteResponse deleteResponse;
    private final ProgressHandler progressHandler;
    private final CanvasHandler canvasHandler;

    public RequestHandler(Controller controller, ProgressHandler progressHandler,CanvasHandler canvasHandler){
        this.controller = controller;
        this.progressHandler = progressHandler;
        this.canvasHandler = canvasHandler;
    }

    public boolean requestDeletion(ImageFile imageFileLeft, ImageFile imageFileRight) throws InterruptedException{

        synchronized (this){
            deleteResponse = null;
            controller.requestDeletion(imageFileLeft, imageFileRight);
            wait();
            //TODO
        }

        return true;
    }

    public void requestDrawLeft(final Image image){
        Platform.runLater(new Runnable() {
            public void run() {
                canvasHandler.drawLeft(image);
            }
        });
    }

    public void requestDrawRight(final Image image){
        Platform.runLater(new Runnable() {
            public void run() {
                canvasHandler.drawRight(image);
            }
        });
    }

    public void requestDrawLeftAndUpdateProgress(final Image image){
        Platform.runLater(new Runnable() {
            public void run() {
                progressHandler.updateProgress();
                canvasHandler.drawLeft(image);
            }
        });
    }

    public void requestDrawRightAndUpdateProgress(final Image image){
        Platform.runLater(new Runnable() {
            public void run() {
                progressHandler.updateProgress();
                canvasHandler.drawRight(image);
            }
        });
    }

    public void requestInitializeProgressActions(final int numberOfActions){
        Platform.runLater(new Runnable() {
            public void run() {
                progressHandler.setActions(numberOfActions);
            }
        });
    }

}
