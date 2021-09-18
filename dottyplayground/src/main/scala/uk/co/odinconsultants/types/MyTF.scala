package uk.co.odinconsultants.types

import io.kjaer.compiletime._

object MyTF {
  type TwoXTwo = 2 #: 2 #: SNil
  val twoXtwo = 2 #: 2 #: SNil
  val twoXthree = 2 #: 3 #: SNil
  val threeXthree = 3 #: 3 #: SNil

  def twoByTwo[F[_]](x: F[TwoXTwo]): Unit = println("2 x 2")

  def multiply[T <: Int & Singleton](x: 2 #: T #: SNil, y: T #: 3 #: SNil): Unit = println(s"Multiply $x by $y")

  def main(args: Array[String]): Unit = {
    println(twoXtwo.getClass.getName)
    twoByTwo(List(twoXtwo))
//    twoByTwo(List(twoXthree)) // doesn't compile - as expected
    multiply(twoXtwo, twoXthree)
//    multiply(threeXthree, twoXthree) // doesn't compile - as expected
  }
}
