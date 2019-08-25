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
    private ProgressRunnable progressRunnable;
    private DrawLeftRunnable drawLeftRunnable;
    private DrawRightRunnable drawRightRunnable;
    private ProgressResetRunnable progressResetRunnable;

    public RequestHandler(Controller controller, ProgressHandler progressHandler,CanvasHandler canvasHandler){
        this.controller = controller;
        this.progressHandler = progressHandler;
        this.canvasHandler = canvasHandler;
        progressRunnable = new ProgressRunnable();
        drawLeftRunnable = new DrawLeftRunnable();
        drawRightRunnable = new DrawRightRunnable();
        progressResetRunnable = new ProgressResetRunnable();
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

    public void ClearLeftCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                canvasHandler.clearLeft();
            }
        });
    }
    public void ClearRightCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                canvasHandler.clearRight();
            }
        });

    }

    public void DrawLeft(final Image image) {
        drawLeftRunnable.setImage(image);
        Platform.runLater(drawLeftRunnable);
    }

    public void DrawRight(final Image image){
        drawRightRunnable.setImage(image);
        Platform.runLater(drawRightRunnable);

    }

    public void DrawLeftAndUpdateProgress(final Image image){

        drawLeftRunnable.setImage(image);
        Platform.runLater(drawLeftRunnable);
        Platform.runLater(progressRunnable);
    }

    public void DrawRightAndUpdateProgress(final Image image){

        drawRightRunnable.setImage(image);
        Platform.runLater(drawRightRunnable);
        Platform.runLater(progressRunnable);
    }

    public void ProgressUpdate(){
        Platform.runLater(progressRunnable);
    }

    public void ProgressReset(){
        Platform.runLater(progressResetRunnable);
    }

    public void InitializeProgressActions(final int numberOfActions){

        Platform.runLater(new Runnable() {
            public void run() {
                progressHandler.setActions(numberOfActions);
            }
        });
    }




    protected class ProgressRunnable implements Runnable{

        @Override
        public void run() {
            progressHandler.updateProgress();
        }
    }

    protected class ProgressResetRunnable implements Runnable{

        @Override
        public void run() {
            progressHandler.resetProgress();
        }
    }

    protected class DrawLeftRunnable implements Runnable{
        Image image;

        public void setImage(Image image){
            this.image = image;
        }

        @Override
        public void run() {
            canvasHandler.drawLeft(image);
        }
    }

    protected class DrawRightRunnable implements Runnable{
        Image image;

        public void setImage(Image image){
            this.image = image;
        }

        @Override
        public void run() {
            canvasHandler.drawRight(image);
        }
    }
}
