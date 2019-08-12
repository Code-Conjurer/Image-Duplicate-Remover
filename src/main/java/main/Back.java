package main;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class Back {

    //TODO: reduce coupling by removing controller
    public static void execute(File selectedDirectory, Controller controller){
        File[] imageFilesDir = selectedDirectory.listFiles(new ImageFileFiler());
        ProgressHandler progressHandler = new ProgressHandler(imageFilesDir.length, controller);
        CanvasHandler canvasHandler = new CanvasHandler(controller);

        ArrayList<ImageFile> imageFiles = new ArrayList<ImageFile>(0);

        for(File f : imageFilesDir){
            imageFiles.add(new ImageFile(f));
        }

        for(ImageFile i : imageFiles){

            canvasHandler.drawLeft(i.getImage());

            for(ImageFile j : imageFiles){

                canvasHandler.drawRight(j.getImage());

                if(i.isMatch(j))
                    System.out.println(i.getFile().getName() + " -+-+-+-+-+-+-+-+-+-+-+- " + j.getFile().getName());
            }
            progressHandler.updateProgress();
        }
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