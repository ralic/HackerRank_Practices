package org.raliclo.CodeMaster;

/**
 * Created by raliclo on 9/15/16.
 * Project Name : TestNG-1
 */

public class numberOfTriangles {

    public static void main(String[] args) {
        int[] tmp = {1, 2, 3};
        System.out.println(numberOfTriangles(tmp));
    }

    static int numberOfTriangles(int[] sticks) {
        int res = 0;
        for (int i = 0; i < sticks.length; i++) {
            int k = i + 2;
            for (int j = i + 1; j < sticks.length; j++) {
                while (k < sticks.length && sticks[k] < sticks[i] + sticks[j]) {
                    k++;
                }
                res += k - j - 1;
            }
        }
        return res;
    }


}
