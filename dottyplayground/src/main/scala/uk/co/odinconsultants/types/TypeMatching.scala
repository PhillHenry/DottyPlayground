package uk.co.odinconsultants.types
import scala.compiletime.ops.int.{+, <, <=, S, *}
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int

object TypeMatching {

  extension [X <: Int, Y <: Int](x: Int) {
    infix def add(y: Y): X + Y = (x + y).asInstanceOf[X + Y]
    infix def sub(y: Y): X - Y = (x - y).asInstanceOf[X - Y]
    infix def mul(y: Y): X * Y = (x * y).asInstanceOf[X * Y]
    infix def lt(y: Y): X < Y = (x < y).asInstanceOf[X < Y]
    infix def le(y: Y): X <= Y = (x <= y).asInstanceOf[X <= Y]
  }

  trait Shape extends Product with Serializable

  trait SNil extends Shape

  type NumElements[X <: Shape] <: Int = X match {
    case SNil         => 1
    case head #: tail => head * NumElements[tail]
  }

  def main(args: Array[String]): Unit = {
    println("compiles")
  }
}
