package main;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.File;



public class ImageFile{

    final static double WIDTH  = 512;
    final static double HEIGHT = 512;

    private File file;
    private Image image;
    private PixelReader pixelReader;
    private boolean isDeleted = false;

    public ImageFile (File file){
        this.file = file;
        image = new Image(file.toURI().toString(), WIDTH, HEIGHT, false, true);
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

        /*if(otherImage.getWidth() != image.getWidth() || otherImage.getHeight() != image.getHeight()){
            ImageView tempImageView = new ImageView(otherImage);
            tempImageView.setFitHeight(image.getHeight());
            tempImageView.setFitWidth(image.getWidth());
            otherImage = tempImageView.
        }*/

        PixelReader otherPixelReader = otherImage.getPixelReader();
        double colorVarianceRed = 0;
        double colorOtherVarianceRed = 0;
        double colorVarianceGreen = 0;
        double colorOtherVarianceGreen = 0;
        double colorVarianceBlue = 0;
        double colorOtherVarianceBlue = 0;
        Color tempColor, otherTempColor;

        for(int j = 0 ; j < image.getHeight(); j++){
            for(int i = 0; i < image.getWidth(); i++){
                tempColor = pixelReader.getColor(i,j);
                otherTempColor = otherPixelReader.getColor(i,j);
                colorVarianceRed += tempColor.getRed() - otherTempColor.getRed();
                colorOtherVarianceRed += tempColor.getRed() - otherTempColor.getRed();
                colorVarianceGreen += tempColor.getGreen() - otherTempColor.getGreen();
                colorOtherVarianceGreen += tempColor.getGreen() - otherTempColor.getGreen();
                colorVarianceBlue += tempColor.getBlue() - otherTempColor.getBlue();
                colorOtherVarianceBlue += tempColor.getBlue() - otherTempColor.getBlue();
            }
        }

        //colorVarianceRed /= HEIGHT * WIDTH;
        //colorVarianceGreen /= HEIGHT * WIDTH;
        //colorVarianceBlue /= HEIGHT * WIDTH;
        //colorVariance /= image.getHeight() * image.getWidth();
        System.out.println((colorVarianceRed - colorOtherVarianceRed) + " " + (colorVarianceGreen - colorOtherVarianceGreen) + " " + (colorVarianceBlue - colorOtherVarianceBlue));
            return true;
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
        /*if(image.getHeight() != otherImage.getHeight() || image.getWidth() != otherImage.getWidth()){
            //System.out.println("DEBUG: mismatch dimensions");
            return false;
        }*/

        return sameImage(otherImage);
    }
}
