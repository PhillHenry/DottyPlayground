import scala.sys.process._

val scala3Version = "3.0.2"
lazy val scalaTest = ("org.scalatest" %% "scalatest" % "3.2.9")
lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

  )
libraryDependencies += "org.emergent-order" %% "onnx-scala-backends" % "0.14.0"
//libraryDependencies += "org.emergent-order" %% "onnx-scala-backends" % "0.13.0"
libraryDependencies += "org.sciscala" %% "ndscala-onnx-scala" % "0.1.0-SNAPSHOT"
libraryDependencies += scalaTest % Test

lazy val myProject = project
  .settings(
    fork := true,
    javaOptions += s"-Djna.library.path=${"python3-config --prefix".!!.trim}/lib",
    libraryDependencies += "io.kjaer" %% "tf-dotty" % "insert-version-here"
  )
