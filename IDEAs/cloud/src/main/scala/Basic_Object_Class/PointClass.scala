package Basic_Object_Class

/**
  * Created by raliclo on 8/30/16.
  * Project Name : Default (Template) Project
  */


object PointObj {
  def main(args: Array[String]) {
    val pt = new PointClass(10, 20);

    // Move to a new location
    pt.move(10, 10);
  }
}

class PointClass(val xc: Int, val yc: Int) {
  var x: Int = xc
  var y: Int = yc

  def move(dx: Int, dy: Int) {
    x = x + dx
    y = y + dy
    println("Point x location : " + x);
    println("Point y location : " + y);
  }
}