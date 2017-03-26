package IBM20161110.lesson

package IBM20161110.lesson.lesson1_Samples0

/**
  * Created by raliclo on 18/11/2016.
  * Project Name : TestNG-1
  */
class lesson_1_IBMScala {

}


object lesson_1_IBMScala {
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
