package main;

enum DeleteResponse{
    LEFT, RIGHT, SKIP
        }

public class RequestHandler{

    private Controller controller;
    private boolean isWaiting;

    public RequestHandler(Controller controller){
        this.controller = controller;
    }

    public boolean requestDeletion(ImageFile imageFileLeft, ImageFile imageFileRight) throws InterruptedException{

        synchronized (this){
            isWaiting = true;
            wait();

        }

        return true;
    }

    public boolean isWaiting(){
        return isWaiting;
    }
}
