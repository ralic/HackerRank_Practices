package LeetCode;

import java.util.*;

/**
 * Created by raliclo on 02/03/2017.
 */
public class Q127_WordLadder {

    static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>();

        for (String word : wordList) {
            dict.add(word);
        }

        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        int count = 1, dist = 1;

        while (!queue.isEmpty()) {
            String top = queue.poll();
            count--;
            if (top.equals(endWord)) {
                return dist;
            }

            char[] topArr = top.toCharArray();
            for (int i = 0; i < topArr.length; i++) {
                char c = topArr[i];
                for (int j = 0; j < 26; j++) {
                    topArr[i] = (char) ('a' + j);
                    String newStr = String.valueOf(topArr);
                    if (dict.contains(newStr)) {
                        queue.offer(newStr);
                        dict.remove(newStr);
                    }
                }
                topArr[i] = c;
            }

            if (count == 0) {
                count = queue.size();
                dist++;
            }
        }

        return 0;
    }

    public static void main(String[] args) {


        String[] data1 = {"hot", "dot", "dog", "lot", "log", "cog"};
        List<String> words1 = Arrays.asList(data1);
        String[] data2 = {"hot", "dot", "dog", "lot", "log"};
        List<String> words2 = Arrays.asList(data2);
        String[] data3 = {"hot", "dog", "dot"};
        List<String> words3 = Arrays.asList(data3);

        System.out.println("Shortest Path :" + ladderLength("hit", "cog", words1));
        System.out.println("Shortest Path :" + ladderLength("hit", "cog", words2));
        System.out.println("Shortest Path :" + ladderLength("hot", "dog", words3));

    }

}
