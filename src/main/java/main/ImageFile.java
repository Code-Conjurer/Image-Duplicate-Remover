package main;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageFile{

    //final static double WIDTH  = 512;
    //final static double HEIGHT = 512;

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
