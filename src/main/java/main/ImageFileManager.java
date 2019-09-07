package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: reformat class so that only ImageFiles can activate and deactivate Imagefiles
//TODO: make class thread-safe
public class ImageFileManager {
    private static final int imageFileThreshold = 100;
    private static final int resetThreshold = imageFileThreshold/2;
    private static int activeImageFiles = 0;
    private static boolean thresholdReached = false;

    //returns true if there is room for ImageFile and increments activeImageFiles, returns false otherwise
    public static void activateImageFile(){
        activeImageFiles++;
        if(activeImageFiles % 10 == 0)
            System.out.println("a: " + activeImageFiles + " " + thresholdReached);
        if( isThresholdReached() ) {
            thresholdReached = true;
        }
    }

    public static void deactivateImageFile(){
        if(activeImageFiles % 10 == 0)
            System.out.println("d: " + activeImageFiles+ " " + thresholdReached);

        if(activeImageFiles > 0){
            activeImageFiles--;
        }
        if(activeImageFiles < resetThreshold) {
            thresholdReached = false;
        }

    }

    public static void resetThresholdReached(){
        thresholdReached = false;
    }

    public static int getActiveImageFiles(){
        return activeImageFiles;
    }

    public static boolean isThresholdReached(){
        return activeImageFiles >= imageFileThreshold;
    }

    public static void deactivate(List<ImageFile> list, int fromIndex, int toBeCleared) {
        try {
            for(int i = fromIndex; i >= i - toBeCleared; i--){
                list.get(i).clearImageHash();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            return;
        }
    }

    public static void activate(List<ImageFile> list, int fromIndex, int toBeActivated) {
        try {
            for(int i = fromIndex; i <= i + toBeActivated; i++){
                list.get(i).setImageHash();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            return;
        }catch (IOException e){
        }
    }
}
