package IBM20161110.lesson

/**
  * Created by raliclo on 10/11/2016.
  * Project Name : TestNG-1
  */
object ExpressionBlocks {


  def main(args: Array[String]) {
    var y = {
      var x = 25
      x * 2
    }

    println(y)

    var greeting = {
      "Hello"
    }

    println(greeting)


    //    if (boolean) expression else expression
    var x = 12

    var checkX = {
      println("---- Running checkX Expression Block----")
      if (x > 10) {
        println("more")
      } else {
        println("less")
      }
      "----Finished Running checkX----"
    }

    println(checkX)

    if (x > 12) {
      println("more")
    } else if (x < 12) {
      println("less")
    } else {
      println("about the same")
    }

    var n = 5
    val valid = n match {
      case 3000 => "NOT MATCH"
      case 5 => "MATCH"
    }

    println(valid)

    var status = 200
    // def used for variable expression , var is fixed result.

    def msg1 = {
      status match {
        case 200 => "okay"
        case xx if x < 500 => "odd, got this: " + xx
        case xx => "error! got this error status: " + xx
      }
    }

    println(msg1)
    status = 600
    println(msg1)

    status = 200
    println(msg1)

    var msg2 = {
      status match {
        case 200 => "okay"
        case xx if x < 500 => "odd, got this: " + xx
        case xx => "error! got this error status: " + xx

      }
    }

    println(msg2)
    status = 600
    println(msg2)
    status = 200
    println(msg2)

  }
}


class ExpressionBlocks {

}
