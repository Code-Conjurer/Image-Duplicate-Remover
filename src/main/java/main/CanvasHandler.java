package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CanvasHandler {

    private Canvas leftCanvas, rightCanvas;
    private CanvasRunnable leftCanvasRunnable, rightCanvasRunnable;
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
        if(image == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

    }

}

abstract class CanvasRunnable implements Runnable {
    protected final CanvasHandler canvasHandler;
    protected Image image;

    protected CanvasRunnable(CanvasHandler canvasHandler) {
        this.canvasHandler = canvasHandler;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    abstract public void run();
}

class LeftCanvasRunnable extends CanvasRunnable{

    public LeftCanvasRunnable(CanvasHandler canvasHandler){
        super(canvasHandler);
    }

    public void run() {
        if(super.image == null) return;
        canvasHandler.drawLeft(super.image);
    }
}

class RightCanvasRunnable extends CanvasRunnable{

    public RightCanvasRunnable(CanvasHandler canvasHandler){
        super(canvasHandler);
    }

    public void run() {
        if(super.image == null) return;
        canvasHandler.drawRight(super.image);
    }
}