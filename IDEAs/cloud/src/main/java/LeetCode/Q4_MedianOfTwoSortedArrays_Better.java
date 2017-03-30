package LeetCode;

import static java.lang.StrictMath.min;

/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

public class Q4_MedianOfTwoSortedArrays_Better {
    static long speedX;

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        int[] nums1 = {1, 2};
        int[] nums2 = {3, 4};

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        double ans2 = findMedianSortedArrays2(nums1, nums2);
        System.out.println(ans2);
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        double ans3 = findMedianSortedArrays3(nums1, nums2);
        System.out.println(ans3);
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        double ans = findMedianSortedArrays(nums1, nums1.length, nums2, nums2.length);
        System.out.println(ans);
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");


    }


    // FAST SOLUTION O(min(m,n));
    public static double findMedianSortedArrays(int A[], int m, int B[], int n) {
        if (m > n) return findMedianSortedArrays(B, n, A, m);
        int minidx = 0, maxidx = m, i = 0, j = 0, num1 = 0, mid = (m + n + 1) >> 1, num2;
        while (minidx <= maxidx) {
            i = (minidx + maxidx) >> 1;
            j = mid - i;
            if (i < m && j > 0 && B[j - 1] > A[i]) minidx = i + 1;
            else if (i > 0 && j < n && B[j] < A[i - 1]) maxidx = i - 1;
            else {
                if (i == 0) num1 = B[j - 1];
                else if (j == 0) num1 = A[i - 1];
                else num1 = Math.max(A[i - 1], B[j - 1]);
                break;
            }
        }
        if (((m + n) & 1) == 1) return num1;
        if (i == m) num2 = B[j];
        else if (j == n) num2 = A[i];
        else num2 = min(A[i], B[j]);
        return (num1 + num2) / 2.;
    }


    public static double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        int n = nums1.length, m = nums2.length;
        int med1 = 0, med2 = 0, i = 0, j = 0;
        while (i + j <= (m + n) / 2) {
            if (i < n && j < m) {
                med2 = med1;
                if (nums1[i] < nums2[j]) {
                    med1 = nums1[i];
                    i++;
                } else {
                    med1 = nums2[j];
                    j++;
                }
            } else if (i < n) {
                med2 = med1;
                med1 = nums1[i];
                i++;
            } else if (j < m) {
                med2 = med1;
                med1 = nums2[j];
                j++;
            }

        }
        if ((m + n) % 2 == 0) return (med1 + med2) / 2.0;
        return med1;
    }


    // SLOW SOLUTION
    public static double findMedianSortedArrays3(int[] nums1, int[] nums2) {
        int[] num = new int[nums1.length + nums2.length];
        for (int i = 0, j = 0, k = 0; i < num.length; i++) {
            if (j < nums1.length && k < nums2.length) {
                num[i] = nums1[j] < nums2[k] ? nums1[j++] : nums2[k++];
            } else if (j < nums1.length)
                num[i] = nums1[j++];
            else if (k < nums2.length)
                num[i] = nums2[k++];
        }
        if (num.length == 1)
            return num[0];
        double result = num.length % 2 == 0 ? ((double) num[num.length / 2 - 1] + (double) num[num.length / 2]) / 2 : (double) num[num.length / 2];
        return result;
    }
}
