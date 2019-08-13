package main;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;

import java.io.IOException;

public class ImageFileMatcher {

    final static int KEY_BIT_RESOLUTION = 64;
    final static HashingAlgorithm hasher = new AverageHash(KEY_BIT_RESOLUTION);

    public static HashingAlgorithm getHasher(){
        return hasher;
    }

    public static boolean isDuplicate(Hash hash1, Hash hash2){

        return isDuplicateAlgo(hash1, hash2);
    }

    public static boolean isDuplicate(Hash hash1, ImageFile image2) throws IOException {

        Hash hash2 = hasher.hash(image2.getFile());

        return isDuplicateAlgo(hash1, hash2);
    }

    private static  boolean isDuplicateAlgo(Hash hash1, Hash hash2){

        //Compute a similarity score
        // Ranges between 0 - 1. The lower the more similar the images are.
        double similarityScore = hash1.normalizedHammingDistance(hash2);

        return similarityScore < 0.4d;
    }
}