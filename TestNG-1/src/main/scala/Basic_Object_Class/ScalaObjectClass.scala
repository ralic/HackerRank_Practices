package Basic_Object_Class

///**
//  * Created by raliclo on 8/22/16.
//  * Project Name : TestNG-1
//  */
//

/*
http://stackoverflow.com/questions/1755345/difference-between-object-and-class-in-scala

You can think of the object keyword as creating a singleton object of a class that is defined implicitly.


A class is a definition, a description. It defines a type in terms of methods and composition of other types.

An object is a singleton -- an instance of a class which is guaranteed to be unique.

For every object in the code, an anonymous class is created,
which inherits from whatever classes you declared object to implement.

This class cannot be seen from Scala source code -- though you can get at it through reflection.
There is a relationship between object and class.

An object is said to be the companion-object of a class if they share the same name.
When this happens, each has access to methods of private visibility in the other.
These methods are not automatically imported, though.
You either have to import them explicitly, or prefix them with the class/object name.
*/


object ScalaObjectClass {
  def main(args: Array[String]) {
    //    println("Hello, world! Test Run #12345 ")
    //    println("Hello, world! Test Run @23456 ")
    println(func1(3))
    println(func1(10))
    var obj = new ScalaObjectClass()

    println("proto1(3)", obj.proto1(3))
    println("proto1(10)=" + obj.proto1(10))

    println("proto2(3)", obj.proto2(3))
    println("proto2(10)=" + obj.proto2(10))

    println("func2(obj)=" + func2(obj))

    println("obj.proto3(obj,5)=" + obj.proto3(obj, 5))
  }

  // object ScalaClass can see private members of class ScalaClass

  // Scope shared with Class
  // definition of func1 in object
  private def func1(x: Int) = x * x

  def func2(x: ScalaObjectClass) = {
    import x._
    x.o * o // fully specified and imported
  }
}


class ScalaObjectClass {

  def proto2(x: Int) = proto1(x)

  // Methods
  // class ScalaClass can see private members of object ScalaClass
  // Shared with Class
  // Prefix to call
  def proto1(x: Int) = ScalaObjectClass.func1(x) // Same as func1 in object

  //  Import and use

  def proto3(x: ScalaObjectClass, y: Int): Int = {
    return x.o * y
  }

  // Variables
  private def o = 2
}
