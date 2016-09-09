package ScalaTest

import org.scalatest.testng.TestNGSuite
import org.testng.Assert._
import org.testng.annotations.{BeforeMethod, Test}

import scala.collection.mutable.ListBuffer

class SimpleTest extends TestNGSuite {
  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  @BeforeMethod
  def initialize() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }

  @Test def verifyEasy() {
    // Uses ScalaTest assertions
    sb.append("easy !")
    assert(sb.toString === "ScalaTest is easy !")
    assert(lb.isEmpty)
    lb += "sweet !"
    println(lb)
    assert(lb === ListBuffer("sweet !"))
    println(lb)
  }

  @Test def verifyFun() {
    sb.append("fun!")
    println(sb)
    println(lb)
    assertEquals(sb.toString, "ScalaTest is fun!")
  }

  @Test def verifyShould() {
    println(lb)
    assertEquals(lb, ListBuffer())
    lb += "sweet"
    lb += "fun"
    assertEquals(ListBuffer("sweet", "fun"), lb)
    lb.remove(0)
    println("lb should be fun " + assertEquals(ListBuffer("fun"), lb))
    println(lb)
    while (lb.length > 0) {
      lb.remove(lb.length - 1)
    }
    println(lb)
    println("lb should be Empty ? " + assert(lb.isEmpty))
  }

  @Test def verifyException(): Unit = {
    println("Exception shall be from " + intercept[StringIndexOutOfBoundsException]("concise".charAt(-1)))
  }

}