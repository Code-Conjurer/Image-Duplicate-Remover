package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

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
        image = new Image(file.toURI().toString(), WIDTH, HEIGHT, false, false);
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
        double colorVariance = 0;

        for(int j = 0 ; j < image.getHeight(); j++){
            for(int i = 0; i < image.getWidth(); i++){
                colorVariance += pixelReader.getArgb(i, j) - otherPixelReader.getArgb(i,j);
            }
        }
        colorVariance /= image.getHeight() * image.getWidth();
        //System.out.println("color variance " + colorVariance);
        if(colorVariance < 1 && colorVariance > -1)
            return true;
        else
            return false;
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
