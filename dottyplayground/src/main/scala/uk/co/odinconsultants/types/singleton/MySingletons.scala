package uk.co.odinconsultants.types.singleton

import uk.co.odinconsultants.types.singleton.MySingletons.MyInt
import scala.util.Try

object MySingletons {

  type myPhrase = "old fashioned way" | "old fashioned way 2"

  type MyInt = Int & Singleton

  def testPhrase(x: myPhrase): Unit = println(x)

  def testHKTs[T <: List[MyInt]](xs: T, ys: T): Unit = println(s"xs = $xs, ys = $ys")

  def testSameInts(x: MyInt, y: MyInt): Unit = println(s"$x == $y")

  type MyBinary = 1 | 0

  def accept(binary: MyBinary): Unit = println(s"$binary is binary")

  def main(args: Array[String]): Unit = {
    testPhrase("old fashioned way")
    testPhrase("old fashioned way 2")
//    test("old fashioned way 3") // Fantastic! Does not compile
    testSameInts(1, 2)
    testHKTs(List(1), List(2))
    accept(1)
    accept(0)
//    accept(2) // Fantastic! Does not compile
    val maybeBinary = Try {
      args(0).toInt
    }
//      .map { i => accept(i)} // does not compile as we cannot guarantee it's 1 or 0
  }

}
