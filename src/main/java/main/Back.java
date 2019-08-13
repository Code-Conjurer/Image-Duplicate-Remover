package main;

import com.github.kilianB.hash.Hash;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class Back {

    //TODO: reduce coupling by removing controller
    public static void execute(File selectedDirectory, Controller controller) {
        File[] imageFilesDir = selectedDirectory.listFiles(new ImageFileFiler());

        ProgressHandler progressHandler = new ProgressHandler(imageFilesDir.length, controller);
        CanvasHandler canvasHandler = new CanvasHandler(controller);

        ArrayList<ImageFile> images = new ArrayList<ImageFile>(0);

        for (File f : imageFilesDir) {
            images.add(new ImageFile(f));
        }

        try {
            for (ImageFile image1 : images) {
                Hash hash1 = ImageFileMatcher.getHasher().hash(image1.getFile());

                for (ImageFile image2 : images) {
                    if (!(image1.isMarkedForDeletion() || image2.isMarkedForDeletion()) && image1.getFile() != image2.getFile()) {
                        if (ImageFileMatcher.isDuplicate(hash1, image2)) {

                            System.out.println(image1.getFile().getName() + " " + image2.getFile().getName());
                            RequestDeletion(image2);
                            canvasHandler.drawLeft(image1.getImage());
                            canvasHandler.drawRight(image2.getImage());

                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void RequestDeletion(ImageFile image){
        image.markForDeletion();
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