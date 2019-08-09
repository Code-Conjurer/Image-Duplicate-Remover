package sample;

import javafx.scene.canvas.Canvas;

public class CanvasHandler {

    private Canvas leftCanvas, rightCanavs;

    public CanvasHandler(Controller controller){
        leftCanvas = controller.getLeftCanvas();
        rightCanavs = controller.getRightCanvas();
    }
}
