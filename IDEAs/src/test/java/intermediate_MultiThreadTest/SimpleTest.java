package intermediate_MultiThreadTest;

import org.testng.annotations.*;

public class SimpleTest {


    @BeforeClass
    public static void setUp() {
        TestConfig.setUp();
    }

    @AfterClass
    public static void destroy() {
        TestConfig.destroy();
    }

    @BeforeMethod
    public static void beforeMethod() {
        long id = Thread.currentThread().getId();
        System.out.println(TestConfig.prefixBM + "Before ReadLines-method. Thread id is: " + id);
    }

    @AfterMethod
    public static void afterMethod() {
        long id = Thread.currentThread().getId();
        System.out.println(TestConfig.prefixAM + "After ReadLines-method. Thread id is: " + id);
    }

    @Test(groups = {"slow"})
    public void aSlowTest() {
        TestConfig.countTests++;
        System.out.println(TestConfig.prefixTT + "Slow ReadLines");
    }

    @Test(groups = {"fast"})
    public void aFastTest() {
        TestConfig.countTests++;
        System.out.println(TestConfig.prefixTT + "Fast ReadLines");
    }

    @Test(groups = {"NG"})
    public void aHandsome() {
        TestConfig.countTests++;
        System.out.println(TestConfig.prefixTT + "I love you!");
    }

}
