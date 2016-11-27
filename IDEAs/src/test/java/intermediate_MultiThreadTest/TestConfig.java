package intermediate_MultiThreadTest;

import org.testng.annotations.*;

import java.util.Date;
import java.util.stream.IntStream;

/**
 * Created by raliclo on 7/12/16.
 * Project Name : TestNG-1
 */
public class TestConfig {

    public static String prefixBM = "[BM]-->  ";
    public static String prefixTT = "[TT]#    ";
    public static String prefixMT = "[MT]##   ";
    public static String prefixAM = "[AM]<--  ";
    public static int countTests;
    private static String prefixBT = "[BT]~~~~~";
    private static String prefixBC = "[BC]->   ";
    private static String prefixAC = "[AC]<-   ";
    private static String prefixAT = "[AT]~~~~~";
    private static Date classTime;
    private static String testName = "Ralic Lo";
    //    String testName=null;

    @AfterTest
    public static void afterTest() {
        IntStream.range(1, 60).forEach((i) -> System.out.print("-"));
        System.out.println();
        // testName is "Ralic Lo" if no parameter.
        System.out.println("Total Tests: " + TestConfig.countTests);
        System.out.println(TestConfig.prefixAT + "My Name is " + TestConfig.testName + ", I learned Java TestNG !");

    }

    @BeforeClass
    public static void setUp() {
        // code that will be invoked when this ReadLines is instantiated
        TestConfig.classTime = new Date();
        System.out.println(TestConfig.prefixBC + "BeforeClass : I don't know Java, today is " + TestConfig.classTime);
        IntStream.range(1, 60).forEach((i) -> System.out.print("#"));
        System.out.println();
    }

    @AfterClass
    public static void destroy() {
        // code that will be invoked when this ReadLines is instantiated
        System.out.println(TestConfig.prefixAC + "AfterClass : Time Elapsed: " + (new Date().getTime() - TestConfig.classTime.getTime()) + "ms");
        IntStream.range(1, 60).forEach((i) -> System.out.print("#"));
        System.out.println();
    }

    @BeforeTest
    @Parameters({"ReadLines-name"})
    public void beforeTest(@Optional("Books") String testName) {
        // @Optional
        // testName is "Books" if no parameter from ReadLines-name in xml configuration file.
        long id = Thread.currentThread().getId();
        System.out.println(TestConfig.prefixBT + "Before ReadLines [ReadLines-name]= " + testName + ". Thread id is: " + id);
        IntStream.range(1, 60).forEach((i) -> System.out.print("-"));
        System.out.println();
    }

}
