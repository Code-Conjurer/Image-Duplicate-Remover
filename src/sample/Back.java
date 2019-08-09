package sample;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class Back {


    //TODO: reduce coupling by removing controller
    public static void run(File selectedDirectory, Controller controller){
        File[] imageFilesDir = selectedDirectory.listFiles(new ImageFileFiler());
        ProgressHandler progressHandler = new ProgressHandler(imageFilesDir.length, controller);


        ArrayList<ImageFile> imageFiles = new ArrayList<>(0);

        for(File f : imageFilesDir){
            imageFiles.add(new ImageFile(f));
        }

        for(ImageFile i : imageFiles){

            for(ImageFile j : imageFiles){
                System.out.println(i.isMatch(j) + " " + i.getFile().getName() + " " + j.getFile().getName());
            }
            progressHandler.updatePrograss();
        }

    }
}

class ImageFileFiler implements FileFilter{

    String[] extensions = {"jpeg", "png", "bmp", "jpg"};

    @Override
    public boolean accept(File pathname) {
        for(String ext : extensions){
            if(pathname.getName().toLowerCase().endsWith(ext))
                return true;
        }
        return false;
    }
}