package uk.co.odinconsultants.types

import org.junit.Assert.*
import org.junit.Test
import uk.co.odinconsultants.types.MyTensorShapeDenotation.{Concat, Reverse}
import io.kjaer.compiletime.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.compiletime.ops.int.*
import scala.language.implicitConversions

type MyDimensionDenotation = String & Singleton

final case class ##:[+H <: MyDimensionDenotation, +T <: MyTensorShapeDenotation](head: H, tail: T)
  extends MyTensorShapeDenotation {
  override def toString = head match {
    case _ ##: _ => s"($head) ##: $tail"
    case _       => s"$head ##: $tail"
  }
}

sealed trait MyTensorShapeDenotation extends Product with Serializable {
  import MyTensorShapeDenotation.*

  /** Prepend the head to this */
  def ##:[H <: MyDimensionDenotation, This >: this.type <: MyTensorShapeDenotation](
                                                                                 head: H
                                                                               ): H ##: This =
    uk.co.odinconsultants.types.##:(head, this)

  /** Concat with another shape * */
  def ++(that: MyTensorShapeDenotation): this.type Concat that.type =
    MyTensorShapeDenotation.concat(this, that)

  /** Reverse the dimension list */
  def reverse: Reverse[this.type] = MyTensorShapeDenotation.reverse(this)

  def toSeq: Seq[String] = this match {
    case MyTSNil         => Nil
    case head ##: tail => head +: tail.toSeq
  }
}


sealed trait MyTSNil extends MyTensorShapeDenotation
case object MyTSNil  extends MyTSNil

object MyTensorShapeDenotation {
  def scalar: MyTSNil                                              = MyTSNil
  def vector(length: MyDimensionDenotation): length.type ##: MyTSNil = length ##: MyTSNil
  def matrix(
              rows: MyDimensionDenotation,
              columns: MyDimensionDenotation
            ): rows.type ##: columns.type ##: MyTSNil = rows ##: columns ##: MyTSNil

  def fromSeq(seq: Seq[String]): MyTensorShapeDenotation = seq match {
    case Nil          => MyTSNil
    case head +: tail => head ##: MyTensorShapeDenotation.fromSeq(tail)
  }

  type Concat[X <: MyTensorShapeDenotation, Y <: MyTensorShapeDenotation] <: MyTensorShapeDenotation =
    X match {
      case MyTSNil         => Y
      case head ##: tail => head ##: Concat[tail, Y]
    }

  def concat[X <: MyTensorShapeDenotation, Y <: MyTensorShapeDenotation](x: X, y: Y): Concat[X, Y] =
    x match {
      case _: MyTSNil        => y
      case cons: ##:[x, y] => cons.head ##: concat(cons.tail, y)
    }

  type Reverse[X <: MyTensorShapeDenotation] <: MyTensorShapeDenotation = X match {
    case MyTSNil         => MyTSNil
    case head ##: tail => Concat[Reverse[tail], head ##: MyTSNil]
  }

  def reverse[X <: MyTensorShapeDenotation](x: X): Reverse[X] = x match {
    case _: MyTSNil              => MyTSNil
    case cons: ##:[head, tail] => concat(reverse(cons.tail), cons.head ##: MyTSNil)
  }

  type IsEmpty[X <: MyTensorShapeDenotation] <: Boolean = X match {
    case MyTSNil   => true
    case _ ##: _ => false
  }

  type Head[X <: MyTensorShapeDenotation] <: MyDimensionDenotation = X match {
    case head ##: _ => head
  }

  type Tail[X <: MyTensorShapeDenotation] <: MyTensorShapeDenotation = X match {
    case _ ##: tail => tail
  }

  /** Represents reduction along axes, as defined in TensorFlow:
   *
   *   - None means reduce along all axes
   *   - List of indices contain which indices in the shape to remove
   *   - Empty list of indices means reduce along nothing
   *
   * @tparam S
   *   Shape to reduce
   * @tparam Axes
   *   List of indices to reduce along. `one` if reduction should be done along all axes. `SNil`
   *   if no reduction should be done.
   */
  type Reduce[S <: MyTensorShapeDenotation, Axes <: None.type | Indices] <: MyTensorShapeDenotation =
    Axes match {
      case None.type => MyTSNil
      case Indices   => ReduceLoop[S, Axes, 0]
    }

  /** Remove indices from a shape
   *
   * @tparam RemoveFrom
   *   Shape to remove from
   * @tparam ToRemove
   *   Indices to remove from `RemoveFrom`
   * @tparam I
   *   Current index (in the original shape)
   */
  protected type ReduceLoop[
    RemoveFrom <: MyTensorShapeDenotation,
    ToRemove <: Indices,
    I <: Index
  ] <: MyTensorShapeDenotation = RemoveFrom match {
    case head ##: tail =>
    Indices.Contains[ToRemove, I] match {
      case true  => ReduceLoop[tail, Indices.RemoveValue[ToRemove, I], S[I]]
      case false => head ##: ReduceLoop[tail, ToRemove, S[I]]
    }
    case MyTSNil =>
    ToRemove match {
      case INil => MyTSNil
      //     case head :: tail => Error[
      //         "The following indices are out of bounds: " + Indices.ToString[ToRemove]
      //     ]
    }
  }

  /** Apply a function to elements of a uk.co.odinconsultants.types.MyTensorShapeDenotation. Type-level representation of `def
   * map(f: (A) => A): List[A]`
   *
   * @tparam X
   *   uk.co.odinconsultants.types.MyTensorShapeDenotation to map over
   * @tparam F
   *   Function taking an value of the uk.co.odinconsultants.types.MyTensorShapeDenotation, returning another value
   */
  type Map[
    X <: MyTensorShapeDenotation,
    F[_ <: MyDimensionDenotation] <: MyDimensionDenotation
  ] <: MyTensorShapeDenotation = X match {
    case MyTSNil         => MyTSNil
    case head ##: tail => F[head] ##: Map[tail, F]
  }

  /** Apply a folding function to the elements of a uk.co.odinconsultants.types.MyTensorShapeDenotation Type-level representation
   * of `def foldLeft[B](z: B)(op: (B, A) => B): B`
   *
   * @tparam B
   *   Return type of the operation
   * @tparam X
   *   uk.co.odinconsultants.types.MyTensorShapeDenotation to fold over
   * @tparam Z
   *   Zero element
   * @tparam F
   *   Function taking an accumulator of type B, and an element of type String, returning B
   */
  type FoldLeft[B, X <: MyTensorShapeDenotation, Z <: B, F[_ <: B, _ <: String] <: B] <: B =
    X match {
      case MyTSNil         => Z
      case head ##: tail => FoldLeft[B, tail, F[Z, head], F]
    }
}


class MyTensorCopy {
  type MySupported = Int
  type MyTensorTypeDenotation = String & Singleton
  type MyAxes = Tuple3[MyTensorTypeDenotation, MyTensorShapeDenotation, Shape]
  opaque type MyTensor[T <: MySupported, Ax <: MyAxes] = Tuple2[Array[T], Ax]

  @Test def placeholder(): Unit = {
    println("Hello world")
  }
}
