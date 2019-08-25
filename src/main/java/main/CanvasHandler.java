package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

import java.io.File;

//TODO: add drawHighRes method
public class CanvasHandler {

    private Canvas leftCanvas, rightCanvas;
    private Tooltip leftToolTip, rightToolTip;

    public CanvasHandler(Canvas leftCanvas, Canvas rightCanvas){
        this.leftCanvas = leftCanvas;
        this.rightCanvas = rightCanvas;
        leftToolTip = new Tooltip();
        rightToolTip = new Tooltip();

    }

    public void clearLeft(){
        clear(leftCanvas);
    }

    public void clearRight(){
        clear(rightCanvas);
    }

    public void drawLeft(Image image){
        draw(image, leftCanvas);
    }
    public void drawRight(Image image){
        draw(image, rightCanvas);
    }

    public void setLeftToolTip(String fileName, double width, double height){
        setToolTip(leftToolTip, fileName, width, height);
    }

    public void setLeftToolTip(ImageFile imageFile){
        String fileName = imageFile.getFile().toURI().toString();
        double width = imageFile.getImage().getWidth();
        double height = imageFile.getImage().getHeight();
        setToolTip(leftToolTip, fileName, width, height);
    }

    public void setRightToolTip(String fileName, double width, double height){
        setToolTip(rightToolTip, fileName, width, height);
    }

    public void setRightToolTip(ImageFile imageFile){
        String fileName = imageFile.getFile().toURI().toString();
        double width = imageFile.getImage().getWidth();
        double height = imageFile.getImage().getHeight();
        setToolTip(rightToolTip, fileName, width, height);
    }

    public void hideLeftToolTip(){
        hideToolTip(leftCanvas, leftToolTip);
    }

    public void hideRightToolTip(){
        hideToolTip(rightCanvas, rightToolTip);
    }

    public void showRightToolTip(){
        showToolTip(rightCanvas, rightToolTip);
    }

    public void showLeftToolTip(){
        showToolTip(rightCanvas, rightToolTip);
    }

    private void setToolTip(Tooltip toolTip, String fileName, double width, double height){
        toolTip.setText("DELETE:    " + fileName + "    " + (int)width + " x " + (int)height);
    }

    private void hideToolTip(Canvas canvas, Tooltip toolTip){
        Tooltip.uninstall(canvas, toolTip);
    }

    private void showToolTip(Canvas canvas, Tooltip toolTip){
        Tooltip.install(canvas, toolTip);
    }

    private void clear(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void draw(Image image, Canvas canvas){
        if(image == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

    }



}