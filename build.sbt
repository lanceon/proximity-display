import sbt.Keys._
import sbt._


lazy val root = project in file(".")


lazy val distance = (project in file("modules/distance"))
  .settings(name := "distance")

lazy val lcd = (project in file("modules/lcd"))
  .settings(name := "lcd")

lazy val `proximity-display` = (project in file("examples/proximity-display"))
  .settings(name := "proximity-display")

