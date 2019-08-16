package main;


import java.util.Observable;

enum DeleteResponse{
    LEFT, RIGHT, SKIP
        }

public class RequestHandler{

    private Controller controller;
    private DeleteResponse deleteResponse;

    public RequestHandler(Controller controller){
        this.controller = controller;
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


}
