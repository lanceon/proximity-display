import Common._
import sbt.Keys._
import sbt._



lazy val distance = (project in file("."))
  .settings(sharedProjectSettings: _*)
  .settings(
    name := "distance",
    libraryDependencies ++= piHardwareLibs,
    libraryDependencies ++= Seq(
      "io.reactivex"     %% "rxscala"     % "0.26.0"
    )
  )

