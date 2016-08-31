package Java8_Tests.Spark_UnitTest;/**
 * Created by raliclo on 8/22/16.
 * Project Name : TestNG-1
 */

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Spark_WordCount {

    public static void main(String[] args) throws Exception {
        String inputFile;
        String outputFile;
        // File input and output
        if (args.length == 0) {
            inputFile = "./wordcount_in.txt";
            outputFile = "./wordcount_out.txt";
        } else {
            inputFile = args[0];
            outputFile = args[1];
        }
        // Spark configuration
        SparkConf conf = new SparkConf().setMaster("local").setAppName("wordCount");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load our input data.
        JavaRDD<String> input = sc.textFile(inputFile);
        JavaPairRDD<String, Integer> counts = runETL(input);

        counts.saveAsTextFile(outputFile);
    }

    public static JavaPairRDD<String, Integer> runETL(JavaRDD<String> input) {
        // Split up into suits and numbers and
        // transform into pairs
        JavaPairRDD<String, Integer> suitsAndValues = input.mapToPair(w -> {
            String[] split = w.split("\t");
            int cardValue = Integer.parseInt(split[0]);
            String cardSuit = split[1];
            return new Tuple2<String, Integer>(cardSuit, cardValue);
        });
        JavaPairRDD<String, Integer> counts = suitsAndValues.reduceByKey((x, y) -> x + y);
        return counts;
    }
}
