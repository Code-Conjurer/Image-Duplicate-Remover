package main;

import javafx.scene.control.ProgressBar;

public class ProgressHandler {

    private double actions;
    private ProgressBar progressBar;
    private double currentProgress;
    public ProgressHandler(Controller controller){
        actions = -1;
        this.progressBar = controller.getProgressBar();
    }

    public void setActions(int actions){
        if(actions < 1)
            this.actions = -1;
        this.actions = actions;
    }

    public void resetProgress(){
        if(actions == -1) return;
        progressBar.setProgress(0);
        currentProgress = 0;
    }

    public void updateProgress(){
        if(actions == -1) return;
        double newProgress = currentProgress + (100/actions);
        progressBar.setProgress(newProgress);
    }


}
