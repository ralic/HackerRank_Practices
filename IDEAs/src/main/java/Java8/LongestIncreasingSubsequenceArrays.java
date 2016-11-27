package Java8;/**
 * Created by raliclo on 7/23/16.
 * Project Name : TestNG-1
 */
/*
We define the following:

A subsequence of an array is an ordered subset of the array's elements having the same sequential ordering as the original array. For example, the subsequences of array  are , , , , , , and .
The longest increasing subsequence of an array of numbers is the longest possible subsequence that can be created from its elements such that all elements are in increasing order.
Victoria has two integers,  and . She builds unique arrays satisfying the following criteria:

Each array contains  integers.
Each integer is .
The longest increasing subsequence she can create from the array has length .
Given  pairs of  and  values, print the number of arrays Victoria creates for each pair on a new line. As this number can be quite large, print your answer modulo .

Input Format

The first line contains a single positive integer, , denoting the number of pairs.
Each line  of the  subsequent lines contains two space-separated integers describing the respective  and values for a pair.

Constraints
1<=p<=50
1<=m<=5x10^5
1<=n<=10
n<=m

Output Format
10^9+7

On a new line for each pair, print a single integer denoting the number of different arrays Victoria creates modulo .

Sample Input
2
4 2
4 3
Sample Output

11
9
Explanation

Victoria wants to build arrays of integers having size  where each integer is  and each array has a longest increasing subsequence of length  (i.e., contains the subsequence ). She creates the following eleven arrays:
[1,1,1,2]  1 v
[1,2,1,2]  1 v
[2,1,1,2]  1 v
[2,2,1,2]  1 v

[1,1,2,1]  2 v
[1,1,2,2]  2 v
[2,1,2,1]  2 v
[2,1,2,2]  2 v

[1,2,1,2]    x
[1,2,1,1]    v
[1,2,2,1]    v
[1,2,2,2]    v

Victoria wants to build arrays of integers having size  where each integer is  and each array has a longest increasing subsequence of length  (i.e., contains the subsequence ). She creates the following nine arrays:
[1,1,2,3]  1
[2,1,2,3]  1
[3,1,2,3]  1

[1,2,3,1]  2
[1,2,3,2]  2
[1,2,3,3]  2

[1,2,1,3]  3
[1,2,2,3]  3
[1,2,3,3]  3


 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LongestIncreasingSubsequenceArrays {

    static int[] legal;

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int lines = Integer.parseInt(x.get(0));
        x.remove(0);
        x.forEach((xx) -> ways(xx));
    }

    private static int ways(String xx) {
        int nums;
        int max;
        String[] xxx = xx.split(" ");
        nums = Integer.parseInt(xxx[0]);
        max = Integer.parseInt(xxx[1]);
        if (nums < max) {
            System.out.println(0);
            return 0;
        }
        if (nums == max) {
            System.out.println(1);
            return 1;
        }
        int ans = 0;
        HashMap<String, Integer> bag = new HashMap<>();

        int[] box = new int[nums];
        int i, j, k = 0;
        legal = IntStream.range(1, max + 1).toArray();
        ArrayList<Integer> putindex = new ArrayList<>();

        HashSet<String> testset = StringHelper.brutalIndex(nums, max);
        HashMap<String, Integer> mapper = new HashMap<>();

        ArrayList<char[]> testset3 = new ArrayList<>();
        for (String tmp : testset) {
//            System.out.println(tmp);
            j = 0;
            char[] tmp2 = tmp.toCharArray();
            for (i = 0; i < tmp.length(); i++) {
                if (tmp2[i] == '1') {
//                    System.out.println(i + " " + Character.forDigit(j + 1, 10));
                    tmp2[i] = Character.forDigit(j + 1, 10);
                    j++;
//                }
                }
            }
            System.out.println(Arrays.toString(tmp2));
            testset3.add(tmp2);
        }

//
//        for (i=0;i<testset3.size();i++) {
//            System.out.println(testset3.get(i));
//        }
//
        System.out.println("--");
        return 1;
    }

    private static boolean boxcheck(int[] box, ArrayList<int[]> boxset) {
        for (int[] mm : boxset) {
            for (int i = 0; i < mm.length; i++) {
                if (mm[i] == 0) {
                    boxset.remove(mm);
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean recurbox(int[] box, ArrayList<Integer> permuteindex, HashSet<int[]> boxset, int max) {
        int[] tmp = Arrays.copyOf(box, box.length);
        ArrayList<Integer> tmpPermute = (ArrayList<Integer>) permuteindex.clone();
        boolean flag = true;
        if (permuteindex.size() > 0) {
            int replace = tmpPermute.remove(0);
            for (int i = 0; i < legal.length; i++) {
                tmp[replace] = legal[i];
                boxset.add(tmp);
                if (recurbox(tmp, tmpPermute, boxset, max)) {
                    boxset.add(tmp);
                }
            }
        }
        for (int j = 0; j < tmp.length; j++) {
            if (tmp[j] == 0) {
                return false;
            }
        }
        boxset.add(tmp);
        return true;
    }


    private static class StringHelper {
        public static HashSet<String> permutationFinder(String str) {
            HashSet<String> perm = new HashSet<String>();
            //Handling error scenarios
            if (str == null) {
                return null;
            } else if (str.length() == 0) {
                perm.add("");
                return perm;
            }
            char initial = str.charAt(0); // first character
            String rem = str.substring(1); // Full string without first character
            Set<String> words = permutationFinder(rem);
            for (String strNew : words) {
                for (int i = 0; i <= strNew.length(); i++) {
                    perm.add(charInsert(strNew, initial, i));
                }
            }
            return perm;
        }

        public static String charInsert(String str, char c, int j) {
            String begin = str.substring(0, j);
            String end = str.substring(j);
            return begin + c + end;
        }

        public static HashSet<String> brutalIndex(int digits, int ones) {
            if (ones > digits) {
                return null;
            }
            int[] pseudoIndex = new int[digits];
            for (int i = 0; i < digits; i++) {
                if (i < ones) {
                    pseudoIndex[i] = 1;
                }
            }
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < pseudoIndex.length; i++) {
                b.append(pseudoIndex[i]);
            }
            return permutationFinder(b.toString());
        }
    }
}
