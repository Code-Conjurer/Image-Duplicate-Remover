package main;

import com.github.kilianB.hash.Hash;
import javafx.scene.image.Image;

import com.sun.jna.platform.FileUtils;
import java.io.File;
import java.io.IOException;

public class ImageFile{

    private File file;
    private Hash imageHash;
    private boolean markForDeletion = false;

    public ImageFile (File file) throws IOException{
        this.file = file;
        imageHash = ImageFileMatcher.getHasher().hash(file);
    }

    public Hash getImageHash(){
        return imageHash;
    }

    public File getFile() {
        return file;
    }

    public String getName(){
        return file.getName();
    }

    public Image getImage() {
        return new Image(file.toURI().toString());
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
