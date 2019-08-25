package main;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTask extends Task<Void> {

    private final File selectedDirectory;
    private final RequestHandler requestHandler;

    public MainTask(File selectedDirectory, RequestHandler requestHandler) {
        this.selectedDirectory = selectedDirectory;
        this.requestHandler = requestHandler;
    }

    private File[] createFileArray() {
        return selectedDirectory.listFiles(new ImageFileFiler());
    }

    private ArrayList<Integer> findForMatches(ImageFile imageFile, List<ImageFile> list, Map<String, String> encounteredImageFiles) {
        ArrayList<Integer> indexList = new ArrayList<>(0);
        ImageFile toCompare;
        boolean sameFile, hasEncountered, comparedIsDeleted;
        for (int i = 0; i < list.size(); i++) {
            requestHandler.ProgressUpdate();
            toCompare = list.get(i);

            sameFile = imageFile.getName().equals(toCompare.getName());
            hasEncountered = encounteredImageFiles.get(toCompare.getName()) != null;
            comparedIsDeleted = toCompare.isMarkedForDeletion();
            if ( !(sameFile || hasEncountered || comparedIsDeleted)) { //check that the compared image is not deleted, or has encountered original image
                if (isMatch(imageFile, toCompare))
                    indexList.add(i);
            }
        }

        return indexList;
    }

    private boolean isMatch(ImageFile image1, ImageFile image2) {
        return ImageFileMatcher.isDuplicate(image1.getImageHash(), image2.getImageHash());
    }

    private void handleDeletion(ImageFile leftImageFile, ImageFile rightImageFile, List<String> deletedFiles, Map<String, String> encounteredImageFiles) throws InterruptedException, RequestFailedException{
        switch (requestHandler.requestDeletion(leftImageFile, rightImageFile)) {
            case LEFT:
                requestHandler.ClearLeftCanvas();
                Thread.sleep(100);
                deletedFiles.add(leftImageFile.getFile().toURI().toString());
                leftImageFile.delete();
                break;
            case RIGHT:

                requestHandler.ClearRightCanvas();
                Thread.sleep(100);
                deletedFiles.add(rightImageFile.getFile().toURI().toString());
                rightImageFile.delete();
                break;
            default:
                requestHandler.ClearLeftCanvas();
                requestHandler.ClearRightCanvas();
                Thread.sleep(100);
                encounteredImageFiles.put(leftImageFile.getName(), rightImageFile.getName()); //left has encountered right
                break;
        }
    }

    protected Void call() throws IOException {

        File[] imageFilesDir = createFileArray();

        ArrayList<ImageFile> images = new ArrayList<ImageFile>(0);
        ArrayList<String> deletedFiles = new ArrayList<String>(0);
        HashMap<String, String> encounteredImageFiles = new HashMap<String, String>(0);

        requestHandler.InitializeProgressActions(imageFilesDir.length);

        for (File f : imageFilesDir) {
            images.add(new ImageFile(f));
            requestHandler.ProgressUpdate();
        }
        requestHandler.ProgressReset();
        requestHandler.InitializeProgressActions(imageFilesDir.length * imageFilesDir.length );

        imageFilesDir = null;

        ArrayList<Integer> matchingImageIndexes;
        ImageFile rightImageFile;

        for (final ImageFile leftImageFile : images) {
            if (!leftImageFile.isMarkedForDeletion()) {
                matchingImageIndexes = findForMatches(leftImageFile, images, encounteredImageFiles);

                for (Integer index : matchingImageIndexes) {
                    if(leftImageFile.isMarkedForDeletion()) break;

                    rightImageFile = images.get(index);

                    requestHandler.DrawLeft(leftImageFile.getImage());
                    requestHandler.DrawRight(rightImageFile.getImage());

                    try {

                        handleDeletion(leftImageFile, rightImageFile, deletedFiles, encounteredImageFiles);

                    } catch (InterruptedException e) {
                        System.out.println(e);
                        System.exit(1);
                    } catch (RequestFailedException e) {
                        System.out.println(e);
                        System.exit(1);
                    }
                }
            }
        }

        requestHandler.ClearLeftCanvas();
        requestHandler.ClearRightCanvas();
        requestHandler.enableUI();
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