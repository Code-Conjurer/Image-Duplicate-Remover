package sample;

import javafx.scene.control.ProgressBar;

public class ProgressHandler {

    private double actions;
    private ProgressBar progressBar;
    private double currentProgress;
    public ProgressHandler(int actions, Controller controller){
        this.actions = actions;
        this.progressBar = controller.getProgressBar();
    }

    public void resetProgress(){
        progressBar.setProgress(0);
        currentProgress = 0;
    }

    public void updateProgress(){
        double newProgress = currentProgress + (100/actions);
        progressBar.setProgress(newProgress);
    }


}
