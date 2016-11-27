package IBM20161110.lesson

/**
  * Created by raliclo on 10/11/2016.
  * Project Name : TestNG-1
  */
class lesson4_List {

}

object lesson4_List {
  def main(args: Array[String]): Unit = {

    val colors = List("red", "green", "blue", 1, 2, 3)
    val colors2 = List("red", "green", "blue")

    println(colors(1))
    println(colors.head)
    println(colors.tail)
    println(colors.tail.tail)
    println(colors.tail.tail.tail)

    /* Note : Typically, you should use foreach, not map,
    because map will unnecessarily construct a new List that contains not but Unit instances.
     */
    colors.foreach(println)
    colors.foreach(x => println("Foreach, item:" + x))
    colors.map((item: Any) => println("Map, item:" + item))
    val shorties = colors2.filter(_.size < 5)
    println(shorties) // red,blue
  }
}