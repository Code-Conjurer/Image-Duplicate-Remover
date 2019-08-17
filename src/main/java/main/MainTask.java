package main;

import com.github.kilianB.hash.Hash;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainTask extends Task<Void> {

    private final File selectedDirectory;
    private final RequestHandler requestHandler;

    public MainTask(File selectedDirectory, RequestHandler requestHandler){
        this.selectedDirectory = selectedDirectory;
        this.requestHandler = requestHandler;
    }

    protected Void call() throws Exception {

        File[] imageFilesDir = selectedDirectory.listFiles(new ImageFileFiler());

        requestHandler.requestInitializeProgressActions(imageFilesDir.length);
        //System.out.println(imageFilesDir.length * imageFilesDir.length);
        ArrayList<ImageFile> images = new ArrayList<ImageFile>(0);
        ArrayList<String> deletedFiles = new ArrayList<String>(0);
        HashMap<String, String> encounteredImageFiles = new HashMap<String, String>(0);

        for (File f : imageFilesDir) {
            images.add(new ImageFile(f));
        }

        //TODO introduce procedures to organize code
        String leftImageFileName, rightImageFileName;
        try {
            for (final ImageFile leftImageFile : images) {
                if(!leftImageFile.isMarkedForDeletion()) {
                    Hash hash1 = ImageFileMatcher.getHasher().hash(leftImageFile.getFile());
                    requestHandler.requestDrawLeftAndUpdateProgress(leftImageFile.getImage());
                    leftImageFileName = leftImageFile.getFile().getName();

                    for (final ImageFile rightImageFile : images) {
                        if (!rightImageFile.isMarkedForDeletion()) {
                            rightImageFileName = rightImageFile.getFile().getName();
                            if (!rightImageFileName.equals(leftImageFileName) && encounteredImageFiles.get(leftImageFileName) == null) {
                                requestHandler.requestDrawRight(rightImageFile.getImage());

                                if (ImageFileMatcher.isDuplicate(hash1, rightImageFile)) {
                                    synchronized (this) {
                                        //System.out.println(leftImageFile.getFile().getName() + " " + rightImageFile.getFile().getName());
                                        requestHandler.requestDrawLeft(leftImageFile.getImage());
                                        requestHandler.requestDrawRight(rightImageFile.getImage());
                                        switch (requestHandler.requestDeletion(leftImageFile, rightImageFile)) {
                                            case LEFT:
                                                deletedFiles.add(leftImageFile.getFile().toURI().toString());
                                                leftImageFile.delete();
                                                break;
                                            case RIGHT:
                                                deletedFiles.add(rightImageFile.getFile().toURI().toString());
                                                rightImageFile.delete();
                                            default:
                                                encounteredImageFiles.put(rightImageFileName, leftImageFileName); //right name will be searched on next pass of the outer loop
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (IOException e){
            System.out.println(e);
        }
        requestHandler.requestClearLeftCanvas();
        requestHandler.requestClearRightCanvas();
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