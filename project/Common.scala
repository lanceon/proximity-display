import sbt._
import Keys._

object Common {

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
    version := "1.1-SNAPSHOT",
    libraryDependencies ++= commonLibs
  )

  val commonLibs = {
    Seq(
      "com.typesafe.scala-logging"  %%  "scala-logging"   % "3.1.0",
      "ch.qos.logback"              %   "logback-classic" % "1.1.3"
    )
  }

  val piHardwareLibs = {
    val pi4jVersion = "1.0"
    Seq(
      "com.pi4j"         %  "pi4j-core"   % pi4jVersion,
      "com.pi4j"         %  "pi4j-device" % pi4jVersion
    )
  }



}