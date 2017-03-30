package LeetCode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

public class Q4_MedianOfTwoSortedArrays_BrutalForce {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        int[] nums1 = {1, 2};
        int[] nums2 = {3, 4};
        double ans = findMedianSortedArrays(nums1, nums2);
        System.out.println(ans);
    }


    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        double ans;

        ArrayList<Integer> data = new ArrayList<>();
        Arrays.stream(nums1).forEach(e -> data.add(e));
        Arrays.stream(nums2).forEach(e -> data.add(e));
        data.sort((a, b) -> a.compareTo(b));
        if (data.size() == 1) {
            return data.get(0);
        }
        if (data.size() % 2 == 0) {
            ans = (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2.0;
        } else {
            ans = data.get((data.size() - 1) / 2);
        }
        return ans;
    }

}
