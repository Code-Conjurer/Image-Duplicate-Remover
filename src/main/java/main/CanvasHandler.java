package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class CanvasHandler {

    private Canvas leftCanvas, rightCanvas;
    private Tooltip leftToolTip, rightToolTip;

    public CanvasHandler(Canvas leftCanvas, Canvas rightCanvas){
        this.leftCanvas = leftCanvas;
        this.rightCanvas = rightCanvas;
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

    public void setRightToolTip(String fileName, double width, double height){
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
        toolTip.setText(fileName + "    " + width + "x" + height);
    }

    private void hideToolTip(Canvas canvas, Tooltip toolTip){
        Tooltip.uninstall(canvas, toolTip);
    }

    private void showToolTip(Canvas canvas, Tooltip toolTip){
        Tooltip.install(canvas, toolTip);
    }

    private void draw(Image image, Canvas canvas){
        if(image == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

    }



}