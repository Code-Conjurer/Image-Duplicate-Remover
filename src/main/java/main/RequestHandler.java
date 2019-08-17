package main;


import javafx.application.Platform;
import javafx.scene.image.Image;

enum DeleteResponse{
    LEFT, RIGHT, SKIP
        }

public class RequestHandler{

    private Controller controller;
    private volatile DeleteResponse deleteResponse;
    private boolean isWaiting;
    private final ProgressHandler progressHandler;
    private final CanvasHandler canvasHandler;

    public RequestHandler(Controller controller, ProgressHandler progressHandler,CanvasHandler canvasHandler){
        this.controller = controller;
        this.progressHandler = progressHandler;
        this.canvasHandler = canvasHandler;
        isWaiting = false;
    }

    public DeleteResponse requestDeletion(ImageFile imageFileLeft, ImageFile imageFileRight) throws InterruptedException, RequestFailedException{

        synchronized (this){
            deleteResponse = null;
            isWaiting = true;
            controller.requestDeletion(imageFileLeft, imageFileRight);
            wait();
            isWaiting = false;
            //GUI has responded
            if(deleteResponse == null) throw new RequestFailedException();
            return deleteResponse;
        }
    }

    public void wakeUp(){
        synchronized (this){
            notify();
        }
    }

    public void setDeleteResponse(DeleteResponse deleteResponse){
        this.deleteResponse = deleteResponse;
    }

    public boolean isWaiting(){
        return isWaiting;
    }

    public void requestClearLeftCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                canvasHandler.clearLeft();
            }
        });
    }
    public void requestClearRightCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                canvasHandler.clearRight();
            }
        });
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
