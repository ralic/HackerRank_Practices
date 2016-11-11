package IBM20161110.exercise

/**
  * Created by raliclo on 10/11/2016.
  * Project Name : TestNG-1
  */
class ex1_C2Fexample {

}

object ex1_C2Fexample {
  def main(args: Array[String]): Unit = {
    var c = 22.5;
    var cunit = "degC"
    var funit = "degC"
    var f = c * 9 / 5 + 32
    println("f :" + f)
    println("c :" + (f - 32) * 5 / 9)

    println("1/2 f -> c :" + (f / 2 - 32) * 5 / 9)
  }
}