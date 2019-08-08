package sample;

import java.io.File;
import java.io.FileFilter;

public class Back {


    //TODO: reduce coupling by removing controller
    public static void run(File selectedDirectory, Controller controller){
        File[] imageFiles = selectedDirectory.listFiles(new ImageFileFiler());
        ProgressHandler progressHandler = new ProgressHandler(imageFiles.length, controller);
        
        /*for(File f : imageFiles)
            System.out.println(f.getName());*/


    }
}

class ImageFileFiler implements FileFilter{

    String[] extensions = {"jpeg", "png", "jpg" };

    @Override
    public boolean accept(File pathname) {
        for(String ext : extensions){
            if(pathname.getName().toLowerCase().endsWith(ext))
                return true;
        }
        return false;
    }
}