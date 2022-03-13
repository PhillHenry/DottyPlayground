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
  }

}
