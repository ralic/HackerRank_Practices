package Java8_Tests.ClassTest;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * Created by raliclo on 7/24/16.
 * Project Name : TestNG-1
 */
public class TestNG_PermUtilTest {
    public Integer[] info = {1, 2, 3, 4, 5, 6, 7};

    @Test
    public void testUsage() {
        System.out.println(Arrays.toString(info));

        PermUtil<Integer> xx = new PermUtil(info);
        while (xx.next() != null) {
            System.out.println(Arrays.toString(xx.next()));
        }
    }
}