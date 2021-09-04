import org.junit.Test
import org.junit.Assert.*
//import scala.collection.immutable.ArraySeq
import scala.language.implicitConversions
import org.emergentorder.onnx.Tensors._
import org.emergentorder.compiletime._
import io.kjaer.compiletime._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
//import scala.reflect.ClassTag
import org.sciscala.ndscala.ONNXScalaOps._
import org.sciscala.ndscala.given_NDArrayOps_Tensor

class Test1:
  @Test def t1(): Unit =
    val square_2 = Tensor(Array(1, 2, 3, 4),"TensorTypeDenotation", "TensorShapeDenotation" ##: TSNil, 2 #: 2 #: SNil)
    // Note: you get a java.lang.IllegalArgumentException: requirement failed if the values don't fit the shape
    // NOT a compile error!
    val square_3 = Tensor(Array(1, 2, 3, 4, 5, 6, 7, 8, 9),"TensorTypeDenotation", "TensorShapeDenotation" ##: TSNil, 3 #: 3 #: SNil)
    val product = square_2 * square_2 // compiles :)
//    assertEquals(square_2 * square_3, square_2) // compilation error, as it should be
