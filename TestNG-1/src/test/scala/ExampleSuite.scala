package ScalaTestNG

import org.scalatest.testng.TestNGSuite
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.ListBuffer
import org.testng.annotations.{BeforeMethod, BeforeSuite, BeforeTest, Test}

class ExampleSuite extends TestNGSuite with ShouldMatchers {
  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  @BeforeMethod
  def initialize() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }

  @Test def verifyEasy() {
    // Uses ScalaTest assertions
    sb.append("easy!")
    assert(sb.toString === "ScalaTest is easy!")
    assert(lb.isEmpty)
    lb += "sweet"
    println(lb)
    intercept[StringIndexOutOfBoundsException] {
      "concise".charAt(-1)
    }
  }

    @Test def verifyFun() {
      // Uses ScalaTest matchers
      sb.append("fun!")
      println(sb)
      println(lb)
      sb.toString should be("ScalaTest is fun!")
    }

  @Test def verifyShould() {
    println(lb)
    lb should be(ListBuffer())
    lb += "sweet"
    lb += "fun"
    lb should be(ListBuffer("sweet", "fun"))
    lb.remove(0)
    lb should be(ListBuffer("fun"))
    println(lb)
    while (lb.length > 0) {
      lb.remove(lb.length - 1)
    }
    lb should be(empty)
    println(lb)
    println("lb should be" + assert(lb.isEmpty))

  }

  @Test def verifyException() {
    an[StringIndexOutOfBoundsException] should be thrownBy {
      "concise".charAt(-1)
    }
  }

}