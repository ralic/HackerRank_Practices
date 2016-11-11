package IBM20161110.lesson


/**
  * Created by raliclo on 8/30/16.
  * Project Name : Default (Template) Project
  */
/*
 * Info: Name=Lo,WeiShun
 * Author: raliclo
 * Filename: IBM.scala
 * Date and Time: Nov 10, 2016 8:02:42 PM
 */
/*
 * Copyright 2016 raliclo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


object IBMScala {
  def main(args: Array[String]) {
    println("Hello, world! Test Run @IBM ")

    /* Java
        String name = "Phil";
     */
    var name: String = "Phil";


    /* Java
      int doubler (int amount {
      return amount *2;
      }
     */

    def doubler(amount: Int): Int = amount * 2
    println("Doubler " + doubler(4)) //8


    /* Java
      public class Node {{
        public String Name;
        public Node(Stirng name) {
        this.name = name; }
        overrider public void Strin
        // Getter
        // Setter
        } ....
     */

    /* Scala
       Simplified getter/setter for a class.
     */

    case class Node(name: String) {
      // method definition
      def doubleString(): String = name + " " + name
    }

    val ex1 = Node("Example 1") // val  : it's for constant, not for reaasignment
    var ex2 = Node("Example 2") // var  : it's ok for reassignemnt
    println(ex1)
    println(ex1.name)
    //    ex1.name = "Rename1"  // Error : reassignemnt to val
    //    ex1= "Test1"  // Error : reassignemnt to val
    //    ex1=Node("Test1") // Error : reassignemnt to val
    println(ex1)
    println(ex1.name)

    println(ex2)
    println(ex2.name)
    //    ex2.name = "Rename2"  // Error : reassignemnt to val
    //    ex2= "Test2"  // Error : type mismatch
    ex2 = Node("Test2")
    println(ex2)
    println(ex2.name)
    println(ex2.doubleString())

    print("--00 Hello line=1\n--01 Hello line=2")

    var list1 = List(1, 3, 20) // List[Int]
    val list2 = List('a', 'b', 'c', 1) // List [Char]
    var list3 = List("hi", "there", 4) // List[String]
    //    var list4 = List(x:A , y:A , z : A)  // List[A]
    println(list1)
    println(list2)
    println(list3)

    val bigDecimalNumber = java.math.BigDecimal.valueOf(20.01);
    println("Java BigDecimal" + bigDecimalNumber)

    val jDate = java.time.LocalDate.now();
    println("Time Now" + jDate)

    /*  Scala Generics
    defn : Code or date that takes a type as a parameter specifying a type to use at runtime

    List[A] could be List[Int] , List[String], ...etc
     */

    println("List1= " + list1)
    list1 = list1 :+ 3
    println("list1 :+3 " + list1 + "\n")

    val list4 = list1 ++ list2
    println("list1 ++ list2 " + list4)
    println(list1.++(list2) + "\n")

    val list5 = list1 ::: list3
    println("list1 ::: list3 " + list5)
    println(list1.++(list2) + "\n")


  }
}
