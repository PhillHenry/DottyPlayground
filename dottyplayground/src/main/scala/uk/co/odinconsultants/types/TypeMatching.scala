package uk.co.odinconsultants.types
import scala.compiletime.ops.int.{+, <, <=, S, *}
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int

object TypeMatching {

  trait Shape extends Product with Serializable

  trait SNil extends Shape

  type Dimension = Int & Singleton

  final case class #:[+H <: Dimension, +T <: Shape](head: H, tail: T) extends Shape {
    override def toString = head match {
      case _ #: _ => s"($head) #: $tail" // Dimension really needs to be a Singleton for this to compile
      case _      => s"$head #: $tail"
    }
  }

  type NumElements[X <: Shape] <: Int = X match {
    case SNil         => 1
    case head #: tail => head * NumElements[tail]
  }

  def main(args: Array[String]): Unit = {
    println("compiles")
  }
}
