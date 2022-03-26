import scala.sys.process._

val scala3Version = "3.1.2-RC2"
lazy val scalaTest = ("org.scalatest" %% "scalatest" % "3.2.9")
lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

    scalacOptions ++= Seq("-Xmax-inlines 1024")
  )
libraryDependencies += "org.emergent-order" %% "onnx-scala-backends" % "0.14.0"
//libraryDependencies += "org.emergent-order" %% "onnx-scala-backends" % "0.13.0"
libraryDependencies += "org.sciscala" %% "ndscala-onnx-scala" % "0.1.0-SNAPSHOT"
libraryDependencies += "eu.timepit" %% "refined" % "0.9.28"
libraryDependencies += scalaTest % Test

lazy val myProject = project
  .settings(
    fork := true,
    javaOptions += s"-Djna.library.path=${"python3-config --prefix".!!.trim}/lib",
    resolvers +=
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies += "io.kjaer" %% "tf-dotty" % "0.0.0+53-201c1206-SNAPSHOT"
  )
