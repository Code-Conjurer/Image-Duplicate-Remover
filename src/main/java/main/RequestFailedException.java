package main;

public class RequestFailedException extends Exception {
    public RequestFailedException(){
        super("Request Failed");
    }
}
