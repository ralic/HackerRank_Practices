package Basic

/**
  * Created by raliclo on 8/30/16.
  * Project Name : Default (Template) Project
  */
/*
 * Info: Name=Lo,WeiShun
 * Author: raliclo
 * Filename: HelloWorld.scala
 * Date and Time: Jul 3, 2016 6:02:42 PM
 * Project Name: Expression project.name is undefined on line 8, column 33 in Templates/Licenses/license-default.txt.
 */
/*
 * Copyright 2016 raliclo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class ScalaHelloWorld {
 def show: Unit = {
   println("Show Method")
 }
}

object mainHello {
  def main(args: Array[String]) {
    println("Hello, world! Test Run #12345 ")
    var testHello = new ScalaHelloWorld()
    testHello.show;
    println("Hello, world! Test Run @23456 ")
  }


}
