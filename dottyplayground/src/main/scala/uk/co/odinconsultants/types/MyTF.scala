package uk.co.odinconsultants.types

import io.kjaer.compiletime._
//import io.kjaer.tensorflow.core._ // not sure where to get this from

object MyTF {
  type Dim = 2 #: 2 #: SNil
  val dimensions = 2 #: 2 #: SNil
//  val matrix = tf.zeros(2 #: 2 #: SNil)

  def main(args: Array[String]): Unit = {
    println(dimensions.getClass.getName)
  }
}
