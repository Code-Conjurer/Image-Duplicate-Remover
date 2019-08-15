package main;

import com.github.kilianB.hash.Hash;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class MainTask extends Task<Void> {

    private final File selectedDirectory;
    private final ProgressHandler progressHandler;
    private final CanvasHandler canvasHandler;

    public MainTask(File selectedDirectory, ProgressHandler progressHandler, CanvasHandler canvasHandler){
        this.selectedDirectory = selectedDirectory;
        this.progressHandler = progressHandler;
        this.canvasHandler = canvasHandler;
    }

    protected Void call() throws Exception {

        File[] imageFilesDir = selectedDirectory.listFiles(new ImageFileFiler());

        progressHandler.setActions(imageFilesDir.length * imageFilesDir.length);
        System.out.println(imageFilesDir.length * imageFilesDir.length);
        ArrayList<ImageFile> images = new ArrayList<ImageFile>(0);

        for (File f : imageFilesDir) {
            images.add(new ImageFile(f));
        }

        try {
            for (final ImageFile image1 : images) {
                Hash hash1 = ImageFileMatcher.getHasher().hash(image1.getFile());
                ////////////////////////////////////////////////////////////
                Platform.runLater(new Runnable() {
                    public void run() {
                        progressHandler.updateProgress();
                        canvasHandler.drawLeft(image1.getImage());
                    }
                });
                /////////////////////////////////////////////////////////////

                for (final ImageFile image2 : images) {
                    /////////////////////////////////////////////////////////
                    Platform.runLater(new Runnable() {
                        public void run() {
                            progressHandler.updateProgress();
                            canvasHandler.drawRight(image2.getImage());
                        }
                    });
                    ///////////////////////////////////////////////////////////

                    if (!(image1.isMarkedForDeletion() || image2.isMarkedForDeletion()) && image1.getFile() != image2.getFile()) {
                        if (ImageFileMatcher.isDuplicate(hash1, image2)) {

                            System.out.println(image1.getFile().getName() + " " + image2.getFile().getName());
                            canvasHandler.drawLeft(image1.getImage());
                            canvasHandler.drawRight(image2.getImage());

                        }
                    }
                }
            }
        }catch (IOException e){
            System.out.println(e);
        }
        return null;
    }
}

class ImageFileFiler implements FileFilter{

    String[] extensions = {"jpeg", "png", "bmp", "jpg"};

    public boolean accept(File pathname) {
        for(String ext : extensions){
            if(pathname.getName().toLowerCase().endsWith(ext))
                return true;
        }
        return false;
    }
}