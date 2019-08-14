package main;

import javafx.scene.image.Image;
import java.io.File;

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

    public void markForDeletion(){
        markForDeletion = true;
    }
}
