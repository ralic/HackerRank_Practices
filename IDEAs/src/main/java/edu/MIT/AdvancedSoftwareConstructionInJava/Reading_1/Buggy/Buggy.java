package edu.MIT.AdvancedSoftwareConstructionInJava.Reading_1.Buggy;

import java.io.PrintWriter;

/**
 * Created by raliclo on 21/03/2017.
 */
public class Buggy {
    public static String subsequences(String word) {
        partialSubsequence = "";
        return subsequencesLouis(word);
    }

    private static String partialSubsequence = "";

    public static String subsequencesLouis(String word) {
        if (word.isEmpty()) {
            // base case
            return partialSubsequence;
        } else {
            // recursive step
            String withoutFirstLetter = subsequencesLouis(word.substring(1));
            partialSubsequence += word.charAt(0);
            String withFirstLetter = subsequencesLouis(word.substring(1));
            return withoutFirstLetter + "," + withFirstLetter;
        }
    }

    public static void main(String[] args) {

        // Configuration
        PrintWriter out = new PrintWriter(System.out);

        // Timer for Speed Test
        long speedX = System.currentTimeMillis();
        out.println(subsequencesLouis("xy"));
        out.println(subsequencesLouis("y"));
        out.println(subsequencesLouis(""));
        out.println(subsequencesLouis(""));
        out.println(subsequencesLouis("y"));
        out.println(subsequencesLouis(""));
        out.println(subsequencesLouis(""));
        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }
}
