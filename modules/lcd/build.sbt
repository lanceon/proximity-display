import sbt.Keys._
import sbt._

import Common._

lazy val lcd = (project in file("."))
  .settings(sharedProjectSettings: _*)
  .settings(
    name := "lcd",
    libraryDependencies ++= piHardwareLibs
  )

