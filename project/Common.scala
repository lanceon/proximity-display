import sbt._
import Keys._

object Common {

  // Used by all modules
  val commonLibraries = {
    val pi4jVersion = "1.0"
    Seq(
      "com.typesafe.scala-logging"  %%  "scala-logging"   % "3.1.0",
      "ch.qos.logback"              %   "logback-classic" % "1.1.3",
      "com.pi4j"         %  "pi4j-core"   % pi4jVersion,
      "com.pi4j"         %  "pi4j-device" % pi4jVersion,
      "io.reactivex"     %% "rxscala"     % "0.26.0",
      "com.github.scopt" %% "scopt"       % "3.3.0",
      "org.scalaz"       %% "scalaz-core" % "7.2.0"
    )
  }

  lazy val sharedProjectSettings = Seq(
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-feature",
      "-optimise",
      "-language:postfixOps",
      "-language:implicitConversions"
    ),
    organization := "com.devscala.explorer",
    version := "1.1",
    libraryDependencies ++= commonLibraries
  )

}