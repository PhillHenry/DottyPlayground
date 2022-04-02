package uk.co.odinconsultants.types.metaprogramming

import scala.compiletime.ops.int.*
import scala.compiletime.{erasedValue, constValue, error}
import scala.compiletime.ops.int.ToString

/**
 * ragnar — Today at 12:54 PM
@PhillHenry they are not inferred to be the same type, the former is inferred to be Succ[Nat] the latter is Succ[Succ[Succ[Zero.type]]].
In your specific case, it seems like the recursive call of the toNat method kinda does not work in a transparent way anymore and is just typed as Nat, not as a more precise type.
(and succ does inline matching which works on the static type, not on any runtime types)
 */
object AdamsInline {
  sealed trait Nat
  case object Zero extends Nat
  case class Succ[N <: Nat](n: N) extends Nat

  type ToNat[N <: Int] <: Nat = N match
    case 0 => Zero.type
    case S[n] => Succ[ToNat[n]]

  type ToInt[N <: Nat] <: Int = N match
    case Zero.type => 0
    case Succ[n] => S[ToInt[n]]

  transparent inline def toNat[I <: Int](inline x: I): ToNat[I] =
    inline erasedValue[I] match
      case z: 0 => Zero
      case s: S[_] => Succ(toNat(s - 1)).asInstanceOf

  transparent inline def toInt[N <: Nat](inline x: N): ToInt[N] =
    inline erasedValue[N] match
      case z: Zero.type => 0
      case s: Succ[_] => (toInt(s.n) + 1).asInstanceOf

  def only5(x: ToInt[ToNat[5]]) = println(x)

  // this compiles but I can't call it
  transparent inline def toNatAlt[I <: Int](inline x: I): ToNat[I] =
    inline if (erasedValue[I] == 0) Zero.asInstanceOf else Succ(toNatAlt(x - 1)).asInstanceOf

//  transparent inline def toNat2[I <: Int]: ToNat[I] =
//    inline erasedValue[I] match
//      case z: 0 => Zero
//      case s: S[_] => Succ(toNat2[I]).asInstanceOf


  def main(args: Array[String]): Unit = {
    only5(5)
//    only5(4)  //  Type Mismatch Error
//    println(toNat2[5])
  }
}
