package main;

import com.github.kilianB.hash.Hash;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class MainTask extends Task<Void> {

    private final File selectedDirectory;
    private final RequestHandler requestHandler;

    public MainTask(File selectedDirectory, RequestHandler requestHandler){
        this.selectedDirectory = selectedDirectory;
        //this.progressHandler = progressHandler;
        //this.canvasHandler = canvasHandler;
        this.requestHandler = requestHandler;
    }

    protected Void call() throws Exception {

        File[] imageFilesDir = selectedDirectory.listFiles(new ImageFileFiler());

        requestHandler.requestInitializeProgressActions(imageFilesDir.length * imageFilesDir.length);
        System.out.println(imageFilesDir.length * imageFilesDir.length);
        ArrayList<ImageFile> images = new ArrayList<ImageFile>(0);

        for (File f : imageFilesDir) {
            images.add(new ImageFile(f));
        }

        try {
            for (final ImageFile leftImageFile : images) {
                Hash hash1 = ImageFileMatcher.getHasher().hash(leftImageFile.getFile());
                ////////////////////////////////////////////////////////////
                requestHandler.requestDrawLeftAndUpdateProgress(leftImageFile.getImage());
                /////////////////////////////////////////////////////////////

                for (final ImageFile rightImageFile : images) {
                    requestHandler.requestDrawRightAndUpdateProgress(rightImageFile.getImage());

                    if (!(leftImageFile.isMarkedForDeletion() || rightImageFile.isMarkedForDeletion()) && leftImageFile.getFile() != rightImageFile.getFile()) {
                        if (ImageFileMatcher.isDuplicate(hash1, rightImageFile)) {

                            System.out.println(leftImageFile.getFile().getName() + " " + rightImageFile.getFile().getName());
                            requestHandler.requestDrawLeft(leftImageFile.getImage());
                            requestHandler.requestDrawRight(rightImageFile.getImage());
                            requestHandler.requestDeletion(leftImageFile, rightImageFile);


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