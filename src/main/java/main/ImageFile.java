package main;

import javafx.scene.image.Image;

import com.sun.jna.platform.FileUtils;
import java.io.File;
import java.io.IOException;

public class ImageFile{

    private File file;
    private Image image;
    private boolean markForDeletion = false;

    public ImageFile (File file){
        this.file = file;
        image = new Image(file.toURI().toString());
    }

    public File getFile() {
        return file;
    }

    public Image getImage() {
        return image;
    }

    public boolean isMarkedForDeletion(){
        return markForDeletion ;
    }

    public void delete(){
        FileUtils fileUtils = FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            try {
                fileUtils.moveToTrash(new File[]{file});
            } catch (IOException e) {
                System.out.println("Error while moving the file to trash " + e.getMessage());
            }
        } else {
            file.delete();
            System.out.println("No Trash available");
        }
        markForDeletion();
    }

    private void markForDeletion(){
        markForDeletion = true;
    }
}
