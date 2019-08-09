package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;


public class ImageFile{

    private File file;
    private Image image;
    private PixelReader pixelReader;
    private boolean isDeleted = false;

    public ImageFile (File file){
        this.file = file;
        image = new Image(file.toURI().toString());
        pixelReader = image.getPixelReader();

        System.out.println(image.getHeight() + " " + image.getWidth());
    }

    public PixelReader getPixelReader(){
        return pixelReader;
    }

    public File getFile() {
        return file;
    }

    public Image getImage() {
        return image;
    }

    public boolean getIsDeleted(){
        return isDeleted;
    }

    public void markForDeletion(){
        isDeleted = true;
    }

    private boolean sameImage(Image otherImage){
        PixelReader otherPixelReader = otherImage.getPixelReader();
        double colorVariance = 0;

        for(int j = 0 ; j < image.getHeight(); j++){
            for(int i = 0; i < image.getWidth(); i++){
                colorVariance += pixelReader.getArgb(i, j) - otherPixelReader.getArgb(i,j);
            }
        }
        colorVariance /= image.getHeight() * image.getWidth();
        System.out.println("color variance " + colorVariance);
        return true;////////////////////////////////////////////////
    }

    //TODO: reorganize logic?
    public boolean isMatch(ImageFile otherImageFile){
        if(otherImageFile.getIsDeleted())
            return false;

        Image otherImage = otherImageFile.getImage();

        if(otherImageFile == this) {
            //System.out.println("DEBUG: " + otherImageFile.getFile().getName() + " " + file.getName());
            return false;
        }
        if(image.getHeight() != otherImage.getHeight() || image.getWidth() != otherImage.getWidth()){
            //System.out.println("DEBUG: mismatch dimensions");
            return false;
        }

        if(sameImage(otherImage))
            return true;
        else
            return false;
    }
}
