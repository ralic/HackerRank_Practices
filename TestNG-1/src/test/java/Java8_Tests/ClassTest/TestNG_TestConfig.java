package Java8_Tests.ClassTest;

import org.testng.annotations.*;

import java.util.Date;
import java.util.stream.IntStream;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * Created by raliclo on 7/12/16. Project Name : TestNG-1
 */
public class TestNG_TestConfig {

    public static String prefixBT = "[BT]~~~~~";
    public static String prefixBC = "[BC]->   ";
    public static String prefixBM = "[BM]-->  ";
    public static String prefixTT = "[TT]#    ";
    public static String prefixMT = "[MT]##   ";
    public static String prefixAM = "[AM]<--  ";
    public static String prefixAC = "[AC]<-   ";
    public static String prefixAT = "[AT]~~~~~";
    public static Date classTime;
    public static int countTests;
    public static String testName = "Ralic Lo";
    //    String testName=null;
//    private final org.apache.logging.Logger logger = LogManager.getLogger(TestNG_TestConfig.class);
//
//    @Test
//    public void testLogging() {
//        logger.error("This is a ReadLines");
//    }

    @AfterTest
    public static void afterTest() {
        IntStream.range(1, 60).forEach((i) -> System.out.print("-"));
        System.out.println();
        // testName is "Ralic Lo" if no parameter.
        System.out.println("Total Tests: " + TestNG_TestConfig.countTests);
        System.out.println(TestNG_TestConfig.prefixAT + "My Name is " + TestNG_TestConfig.testName + ", I learned Java TestNG !");

    }

    @BeforeClass
    public static void setUp() {
        // code that will be invoked when this ReadLines is instantiated
        TestNG_TestConfig.classTime = new Date();
        System.out.println(TestNG_TestConfig.prefixBC + "BeforeClass : I don't know Java, today is " + TestNG_TestConfig.classTime);
        IntStream.range(1, 60).forEach((i) -> System.out.print("#"));
        System.out.println();
    }

    @AfterClass
    public static void destroy() {
        // code that will be invoked when this ReadLines is instantiated
        System.out.println(TestNG_TestConfig.prefixAC + "AfterClass : Time Elapsed: " + (new Date().getTime() - TestNG_TestConfig.classTime.getTime()) + "ms");
        IntStream.range(1, 60).forEach((i) -> System.out.print("#"));
        System.out.println();
    }

    @BeforeTest
    @Parameters({"ReadLines-name"})
    public void beforeTest(@Optional("Books") String testName) {
        // @Optional
        // testName is "Books" if no parameter from ReadLines-name in xml configuration file.
        long id = Thread.currentThread().getId();
        System.out.println(TestNG_TestConfig.prefixBT + "Before ReadLines [ReadLines-name]= " + testName + ". Thread id is: " + id);
        IntStream.range(1, 60).forEach((i) -> System.out.print("-"));
        System.out.println();
    }


}
