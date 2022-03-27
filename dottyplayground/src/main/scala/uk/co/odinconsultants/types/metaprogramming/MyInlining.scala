package uk.co.odinconsultants.types.metaprogramming

object MyInlining {
  sealed trait Nat
  case class Zero(x: Int = 0) extends Nat
  case class Succ[N <: Nat](n: N) extends Nat

  transparent inline def toNat(x: Int): Nat =
    inline x match
      case 0 => Zero()
      case 1 => Succ(Zero())
      case _ => Succ(toNat(x - 1))

  def main(args: Array[String]): Unit = {
    println(s"toNat(4)        = ${toNat(4)}")
  }
}
