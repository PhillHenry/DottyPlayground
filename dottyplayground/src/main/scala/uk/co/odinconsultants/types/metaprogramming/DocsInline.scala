package uk.co.odinconsultants.types.metaprogramming

/**
 * From https://dotty.epfl.ch/docs/reference/metaprogramming/inline.html
 */
object DocsInline {
  class A
  class B extends A:
    def m = true

  transparent inline def choose(b: Boolean): A =
    if b then new A else new B

  val obj1 = choose(true)  // static type is A
  val obj2 = choose(false) // static type is B

  // obj1.m // compile-time error: `m` is not defined on `A`
  obj2.m    // OK

  def unknownAtCompileTime(x: Boolean): A = choose(x)

  def main(args: Array[String]): Unit = {
    unknownAtCompileTime(args(0).toBoolean)
//    println("hello, world")
    myInlineMethod(new Child1[1] {})
  }

  trait Parent[E]
  trait Child1[E] extends Parent[E]
  trait Child2[E] extends Parent[E]
  implicit inline def myInlineMethod[E <: Int](x: Parent[E]): Parent[E] = {
    println(x)
    x match {
      case _: Child1[e] =>
        println("Child1")
        x
      case _: Child2[e] =>
        println("Child2")
        x
      case _ =>
        println("didn't match")
        x
    }
  }

}
