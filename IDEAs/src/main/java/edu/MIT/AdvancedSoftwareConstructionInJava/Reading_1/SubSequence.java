package edu.MIT.AdvancedSoftwareConstructionInJava.Reading_1;

/**
 * Created by raliclo on 21/03/2017.
 */
public class SubSequence {

    public static void main(String[] args) {

//    For example, subsequences("abc") might return "abc,ab,bc,ac,a,b,c,". Note the trailing comma preceding the empty subsequence, which is also a valid subsequence.
//    This problem lends itself to an elegant recursive decomposition. Take the first letter of the word. We can form one set of subsequences that include that letter, and another set of subsequences that exclude that letter, and those two sets completely cover the set of possible subsequences.

        // Configuration

        // Timer for Speed Test
        long speedX = System.currentTimeMillis();

        System.out.println(subsequences("abc"));
        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }

    /**
     * @param word consisting only of letters A-Z or a-z
     * @return all subsequences of word, separated by commas,
     * where a subsequence is a string of letters found in word
     * in the same order that they appear in word.
     */
    public static String subsequences(String word) {
        if (word.isEmpty()) {
            return ""; // base case
        } else {
            char firstLetter = word.charAt(0);
            String restOfWord = word.substring(1);

            String subsequencesOfRest = subsequences(restOfWord);

            String result = "";
            for (String subsequence : subsequencesOfRest.split(",", -1)) {
                result += "," + subsequence;
                result += "," + firstLetter + subsequence;
            }
            result = result.substring(1); // remove extra leading comma
            return result;
        }
    }
}
