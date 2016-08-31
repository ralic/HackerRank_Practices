package Java8_Tests.Spark_UnitTest;/**
 * Created by raliclo on 8/30/16.
 * Project Name : TestNG-1
 */

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Cloudera_SparkWordCount {
    public static void main(String[] args) {

        // create Spark context with Spark configuration
        JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("Spark Count").setMaster("local"));
        JavaRDD<String> lines = sc.textFile("./wordcount_in.txt")
                .filter(s -> s.contains("Diamonds"));
        long numErrors = lines.count();
        System.out.println(numErrors);
    }
}