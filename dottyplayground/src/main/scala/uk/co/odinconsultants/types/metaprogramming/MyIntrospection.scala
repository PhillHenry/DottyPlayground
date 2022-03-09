package uk.co.odinconsultants.types.metaprogramming
import scala.quoted.*

class MyIntrospection {
  def helloWorld(): Unit = println("hello, world")
}

object MyIntrospection {

  def methodsInFileOrder[B: Type](b: B)(using Quotes) = {
    import quotes.reflect.*
    import util.*
    TypeRepr
      .of[B]
      .typeSymbol
      .memberMethods
//      .filter(theOnesIWant)
      .sortBy(_.pos.map(_.start))
  }

  def main(args: Array[String]): Unit = {
    println("Can't run introspection")
    // no implicit argument of type quoted.Quotes was found for parameter x$1 of method
    // doIntrospection in object MyIntrospection
//    doIntrospection
  }

  private def doIntrospection(using Quotes) = {
    import quotes.reflect.*
    import util.*
    val x = new MyIntrospection()
    println(methodsInFileOrder(x))
  }
}
