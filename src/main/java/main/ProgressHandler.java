package main;

import javafx.scene.control.ProgressBar;

public class ProgressHandler {

    private double actions;
    private ProgressBar progressBar;
    private double progress;

    public ProgressHandler(ProgressBar progressBar){
        actions = -1;
        progress = 0;
        this.progressBar = progressBar;
    }

    public void setActions(int actions){
        if(actions < 1)
            this.actions = -1;
        this.actions = actions;
    }

    public void resetProgress(){
        if(actions == -1) return;
        progressBar.setProgress(0);
        progress = 0;
    }

    public void updateProgress(){
        if(actions == -1) return;
        progress++;
        //System.out.println(progressBar.getProgress() + " " + progress);
        progressBar.setProgress(progress/actions);
    }


}
