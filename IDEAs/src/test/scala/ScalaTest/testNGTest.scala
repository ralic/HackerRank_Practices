package ScalaTest

/**
  * Created by raliclo on 8/22/16.
  * Project Name : TestNG-1
  */

import org.scalatest.Assertions
import org.testng.Assert._
import org.testng.annotations.{BeforeTest, Test}

import scala.collection.mutable.ListBuffer

class testNGTest extends Assertions {

  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  @BeforeTest
  def initialize() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }

  @Test def verifyEasy() {
    // Uses TestNG-style assertions
    sb.append("easy!")
    assertEquals("ScalaTest is easy!", sb.toString)
    assertTrue(lb.isEmpty)
    lb += "sweet"
    try {
      "verbose".charAt(-1)
      fail()
    }
    catch {
      case e: StringIndexOutOfBoundsException => // Expected
    }
  }

}