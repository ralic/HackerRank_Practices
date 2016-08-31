//import org.apache.spark.{SparkConf, SparkContext}
//import java.util.concurrent.ForkJoinPool
//
//class SparkBasic {
//
//}
//
//object SparkBasic {
//
//  def main(args: Array[String]) {
//
//    val logFile = "./user.txt" // Should be some file on your system
//    val conf = new SparkConf().setAppName("Simple Application")
//                              .setMaster("local")
//                              .set("spark.cores.max", "4")
//    val sc = new SparkContext(conf)
//
//    val logData = sc.textFile(logFile, 2).cache()
//    val numAs = logData.filter(line => line.contains("a")).count()
//    val numBs = logData.filter(line => line.contains("b")).count()
//    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
//
//  }
//}