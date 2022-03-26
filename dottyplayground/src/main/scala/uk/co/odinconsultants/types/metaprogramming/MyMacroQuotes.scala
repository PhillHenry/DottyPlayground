package uk.co.odinconsultants.types.metaprogramming

import scala.quoted.*

object MyMacroQuotes {
  inline def testMacro(inline expr: Seq[Int]): String = ${ testMacroImpl('expr) }

  def testMacroImpl(expr: Expr[Seq[Int]])(using q: Quotes): Expr[String] =
    import q.reflect.*
    ???
}
