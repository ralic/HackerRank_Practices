package IBM20161110.exercise

/**
  * Created by raliclo on 10/11/2016.
  * Project Name : TestNG-1
  */
class ex2_matchString {

}

object ex2_matchString {
  def main(args: Array[String]): Unit = {

    def matchString(input: Any): Any = input match {
      // USE "Any" to check all types.
      case input => if (input.isInstanceOf[String]) {
        println("This is a string")
      } else {
        println("This is not a string")
      }
        input
    }

    def matchType(obj: Any) = obj match {
      case n: Number => n.longValue
      case b: Boolean => if (b) 1 else 0
      case s: String if s.length != 0 && s != "null" => s.toLong
      case _ => null
    }

    println(matchString("Test001"))
    println(matchString(123123))
    println(matchString(true))
    println(matchString(List("omg", 123)))

    //    println(matchType("Test001"))
    //    println(matchType(123123))
    //    println(matchType(true))
    //    println(matchType(List("omg", 123)))
  }
}