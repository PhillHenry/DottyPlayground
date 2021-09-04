import org.junit.Test
import org.junit.Assert.*
//import scala.collection.immutable.ArraySeq
import scala.language.implicitConversions
import org.emergentorder.onnx.Tensors._
import org.emergentorder.compiletime._
import io.kjaer.compiletime._
//import scala.reflect.ClassTag
//import ONNXScalaOps._

class Test1:
  @Test def t1(): Unit = 
    assertEquals("I was compiled by Scala 3. :)", msg)
