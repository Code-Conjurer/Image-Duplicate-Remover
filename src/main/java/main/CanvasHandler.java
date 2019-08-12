package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CanvasHandler {

    private Canvas leftCanvas, rightCanvas;
    //private GraphicsContext leftGC, rightGC;

    public CanvasHandler(Controller controller){
        leftCanvas = controller.getLeftCanvas();
        rightCanvas = controller.getRightCanvas();
        //leftGC = leftCanvas.getGraphicsContext2D();
        //rightGC = rightCanvs.getGraphicsContext2D();
    }

    public void drawLeft(Image image){
        draw(image, leftCanvas);
    }
    public void drawRight(Image image){
        draw(image, rightCanvas);
    }

    private void draw(Image image, Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

    }

}
