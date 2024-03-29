package uk.co.odinconsultants.types.metaprogramming

import scala.compiletime.ops.int.*
import scala.compiletime.{constValue, erasedValue, error}
import scala.compiletime.ops.int.ToString

/**
 * ragnar — Today at 12:54 PM
@PhillHenry they are not inferred to be the same type, the former is inferred to be Succ[Nat] the latter is Succ[Succ[Succ[Zero.type]]].
In your specific case, it seems like the recursive call of the toNat method kinda does not work in a transparent way anymore and is just typed as Nat, not as a more precise type.
(and succ does inline matching which works on the static type, not on any runtime types)
 */
object AdamsInline {
  trait Nat
  case object Zero extends Nat
  case class Succ[N <: Nat](n: N) extends Nat

  type ToNat[N <: Int] <: Nat = N match
    case 0    => Zero.type
    case S[n] => Succ[ToNat[n]]

  type ToInt[N <: Nat] <: Int = N match
    case Zero.type  => 0
    case Succ[n]    => S[ToInt[n]]

  inline def toInt[N <: Nat]: ToInt[N] =
    inline erasedValue[N] match
      case z: Zero.type => 0
      case s: Succ[t]   => toInt[t].asInstanceOf

  def only5(x: ToInt[ToNat[5]]) = println(x)

  inline def toNat[I <: Int]: ToNat[I] =
    inline erasedValue[I] match
      case z: 0    => Zero
      case s: S[t] => Succ(toNat[t])

  val _7 = toNat[7]

  def main(args: Array[String]): Unit = {
    only5(5)
//    only5(4)  //  Type Mismatch Error
    println(toNat[5])
    println(toInt[_7.type])
  }
}

object NoS {
  import uk.co.odinconsultants.types.metaprogramming.AdamsInline.{Nat, Succ, Zero}
  
  type MyS[N <: Int] <: Int
  type MyNat[N <: Int] <: Nat = N match
    case 0      => Zero.type
    case MyS[n] => Succ[MyNat[n]]
  inline def toMyNat[I <: Int]: MyNat[I] =
    inline erasedValue[I] match
      case z: 0       => Zero
      case s: MyS[t]  => Succ(toMyNat[t])

//  val my7 = toMyNat[7] // cannot reduce inline match with
}
