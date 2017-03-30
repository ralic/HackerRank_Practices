package IBM20161110.lesson

/**
  * Created by raliclo on 10/11/2016.
  * Project Name : TestNG-1
  */
class lesson3_Functions {

}

object lesson3_Functions {
  def main(args: Array[String]): Unit = {


    /*
      General Functions
     */
    def area(r: Int) = Math.PI * r * r
    println(area(20))


    def buzz(x: Int): String = {
      "the value is " + x
    }

    val fizz: (Int) => String = buzz

    println(buzz(1231))
    println(fizz(3))

    /*
      Syntax : Function Type
      (type [ , type, ...]) => output-type
     */

    def hello(name: String) = println("Hello " + name)
    hello("World")

    def hello2(name: String) = {
      println("Hello " + name)
    }
    hello2("World")


    /*
      Syntax : Placeholder Syntax

      ( variable : type) => expression block
     */


    val doubler = (x: Int) => x * 2
    println(doubler(123))


    val links1: (Int, Int) => Int = _ + _
    println(links1(123, 456))

    val links2: (Int, String) => Any = _ + _
    println(links2(123, "asdf"))

    //    val links3: (Any, Any) => Any = _ + _ // type mismatch
    //    println(links3(123, "asdf"))

    val userName = "Ralic Lo"
    def stringSafe(s: String, f: String => String) = {
      if (s != null) f(s) else s
    }

    val reverseName = stringSafe(userName, _.reverse);
    println(reverseName)
  }
}