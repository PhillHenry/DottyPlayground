package uk.co.odinconsultants.types

import io.kjaer.compiletime._

object MyTF {
  type TwoXTwo = 2 #: 2 #: SNil
  val twoXtwo = 2 #: 2 #: SNil
  val twoXthree = 2 #: 3 #: SNil

  def twoByTwo[F[_]](x: F[TwoXTwo]): Unit = println("2 x 2")

  def main(args: Array[String]): Unit = {
    println(twoXtwo.getClass.getName)
    twoByTwo(List(twoXtwo))
//    twoByTwo(List(twoXthree)) // doesn't compile - as expected
  }
}
