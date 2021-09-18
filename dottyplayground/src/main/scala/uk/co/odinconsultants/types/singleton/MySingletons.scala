package uk.co.odinconsultants.types.singleton

import uk.co.odinconsultants.types.singleton.MySingletons.MyInt


object MySingletons {

  type myPhrase = "old fashioned way" | "old fashioned way 2"

  type MyInt = Int & Singleton

  def testPhrase(x: myPhrase): Unit = println(x)

  def testHKTs[T <: List[MyInt]](xs: T, ys: T): Unit = println(s"xs = $xs, ys = $ys")

  def testSameInts(x: MyInt, y: MyInt): Unit = println(s"$x == $y")

  def main(args: Array[String]): Unit = {
    testPhrase("old fashioned way")
    testPhrase("old fashioned way 2")
//    test("old fashioned way 3") // Fantastic! Does not compile
    testSameInts(1, 2)
    testHKTs(List(1), List(2))
  }

}