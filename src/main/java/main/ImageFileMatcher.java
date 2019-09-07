package main;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageColorHash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;

import java.io.IOException;

public class ImageFileMatcher{

    final static int KEY_BIT_RESOLUTION = 1 << 10; //2 raised to the power...
    final static HashingAlgorithm hasher = new PerceptiveHash(KEY_BIT_RESOLUTION);

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

        double similarityScore = hash1.normalizedHammingDistance(hash2);

        return similarityScore < 0.4d;
    }
}