package intermediate_MultiThreadTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by raliclo on 7/12/16. Project Name : TestNG-1
 */
public class MultiThreadTest {
    // Add invocationCount , threadPoolSize in ReadLines

    @BeforeClass
    public static void setUp() {
        TestConfig.setUp();
    }

    @AfterClass
    public static void destroy() {
        TestConfig.destroy();
    }

    @Test(threadPoolSize = 10, invocationCount = 17, timeOut = 1000)
    public void testMethod() {
        TestConfig.countTests++; // 1+1+1+17 = 20
        Long id = Thread.currentThread().getId();
        System.out.println(TestConfig.prefixMT + "Test method executing on thread with id: " + id);
    }

}
