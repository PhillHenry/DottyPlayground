package uk.co.odinconsultants.types.metaprogramming

/**
 * See https://blog.rockthejvm.com/type-level-quicksort/
 */
object RockTheJVMPeanoAxioms {
  trait Nat
  class _0 extends Nat
  class Succ[N <: Nat] extends Nat

  type _1 = Succ[_0]
  type _2 = Succ[_1] // Succ[Succ[_0]]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  // name it "LessThan", then refactor to "<"
  trait <[A <: Nat, B <: Nat]
  object < {
    given basic[B <: Nat]: <[_0, Succ[B]] with {}
    given inductive[A <: Nat, B <: Nat](using lt: <[A, B]): <[Succ[A], Succ[B]] with {
      def helloWorld: Unit = println("hello, world")
      def helloWorld2: Unit = println("hello, world, again!")
    }
    def apply[A <: Nat, B <: Nat](using lt: <[A, B]) = lt
  }
  val comparison = <[_1, _3]

  def main(args: Array[String]): Unit = {
    println(comparison)
  }
}
