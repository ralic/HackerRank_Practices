//https://leetcode.com/problems/two-sum/

package LeetCode;

/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

public class Q1_TwoSum {

    public static void main(String[] args) {

        // Configuration

        int[] input = {2, 7, 11, 15};
        int target = 9;

        // Timer for Speed Test
        long speedX = System.currentTimeMillis();

        int[] ans = twoSum(input, target);
        System.out.println(ans[0] + " " + ans[1]);

        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }

    public static int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    ans[0] = i + 1;
                    ans[1] = j + 1;
                    return ans;
                }
            }
        }
        return ans;
    }
}