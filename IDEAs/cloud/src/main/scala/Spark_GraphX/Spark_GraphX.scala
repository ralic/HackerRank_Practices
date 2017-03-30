//TODO Get it work
////// scalastyle:off println
package Spark_GraphX

//
///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//
//// $example on$
//import org.apache.spark.graphx.GraphLoader

// $example off$
//import org.apache.spark.sql.SparkSession

/**
  * Suppose I want to build a graph from some text files, restrict the graph
  * to important relationships and users, run page-rank on the sub-graph, and
  * then finally return attributes associated with the top users.
  * This example do all of this in just a few lines with GraphX.
  *
  * Run with
  * {{{
  * bin/run-example graphx.ComprehensiveExample
  * }}}
  */
//object ComprehensiveExample {
//
//  def main(args: Array[String]): Unit = {
//    // Creates a SparkSession.
//    val spark = SparkSession
//      .builder
//      .appName(StringContext.apply("", "").s(this.getClass.getSimpleName))
//      .getOrCreate()
//    val sc = spark.sparkContext
//
//    // $example on$
//    // Load my user data and parse into tuples of user id and attribute list
//    val users = (sc.textFile("/Users/raliclo/work/@Netbeans/HackerRank_Practices/TestNG-1/src/main/scala/Spark_GraphX/data/user.txt")
//      .map((line: Nothing) => line.split(",")).map((parts: Nothing) => Tuple2.apply(parts.head.toLong, parts.tail)))
//
//    // Parse the edge data which is already in userId -> userId format
//    val followerGraph = GraphLoader.edgeListFile(sc, "data/graphx/followers.txt")
//
//    // Attach the user attributes
//    val graph = followerGraph.outerJoinVertices(users)({
//      case (uid, deg, Some(attrList: Any)) => attrList
//      // Some users may not have attributes so we set them as empty
//      case (uid, deg, scala.None) => Array.empty()[String]
//    })
//
//    // Restrict the graph to users with usernames and names
//    val subgraph = graph.subgraph(vpred = (vid: Nothing) => attr.size.==(2))
//
//    // Compute the PageRank
//    val pagerankGraph = subgraph.pageRank(0.001)
//
//    // Get the attributes of the top pagerank users
//    val userInfoWithPageRank = subgraph.outerJoinVertices(pagerankGraph.vertices)({
//      case (uid, attrList, Some(pr: Any)) => Tuple2.apply(pr, attrList.toList)
//      case (uid, attrList, scala.None) => Tuple2.apply(0.0, attrList.toList)
//    })
//
//    Predef.println(userInfoWithPageRank.vertices.top(5)(Ordering.by((_: Nothing)._2._1)(NotFoundParameter239239239)).mkString("\n"))
//    // $example off$
//
//    spark.stop()
//  }
//}

//
//// scalastyle:on println*/
