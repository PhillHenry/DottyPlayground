package uk.co.odinconsultants.types.metaprogramming

import scala.compiletime.{erasedValue, constValue, error}
import scala.compiletime.ops.int.ToString

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
    val runtimeBoolean: Boolean = args(0).toBoolean
    val runtimeInt: Int = args(1).toInt
    unknownAtCompileTime(runtimeBoolean)
//    println("hello, world")
    myInlineMethod(new Child1[1] {})
    myInlineMethod(new Child2(runtimeInt))

    val _5 = toNat(5)
    val _33 = toNat(33) // needs -Xmax-inlines...
    println(_5)
    println(_33)
    println(toInt(_5))
    println(toInt(_33))
  }

  trait Parent[E]
  trait Child1[E] extends Parent[E]
  case class Child2[E](e: E) extends Parent[E]
  implicit inline def myInlineMethod[E <: Int & Singleton](x: Parent[E]): Parent[E] = {
    inline erasedValue[E] match // inline is needed here or else "method erasedValue is declared as erased, but is in fact used"
      case _: 1 =>
        println("1!")
        x
      case _ =>
        println("didn't match")
        x
  }

  // fails with "inline match can only be used in an inline method"
//  def normalMethod(x: Any) = {
//    inline x match {
//      case _: String => println("it was a string")
//      case _ => println("it was not a string")
//    }
//  }

  sealed trait Nat
  case object Zero extends Nat
  case class Succ[N <: Nat](n: N) extends Nat

  transparent inline def toNat(x: Int): Nat =
    inline x match
      case 0 => Zero
      case 1 => Succ(Zero)
      case _ => Succ(toNat(x - 1))


  transparent inline def toInt(inline n: Nat): Int =
    inline n match
      case Succ(n1) => toInt(n1) + 1
      case Zero     => 0
      case _        => -1 // wut? Needed to compile

  inline val natTwo = toInt(Succ(Succ(Zero)))
  val intTwo: 2 = natTwo
}
