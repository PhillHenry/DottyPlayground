package uk.co.odinconsultants.types.metaprogramming

import scala.quoted.*

object MyMacroQuotes {
  inline def testMacro(inline expr: Seq[Int]): String = ${ testMacroImpl('expr) }

  def testMacroImpl(expr: Expr[Seq[Int]])(using q: Quotes): Expr[String] =
    import q.reflect.*
    ???

//    def main(args: Array[String]): Unit = testMacro(Seq(1,2,3))
}
