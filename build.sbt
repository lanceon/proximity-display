import java.io.File

import deployssh.DeploySSH.ArtifactSSH
import deployssh.DeploySSH.Keys._
import jassh.SSH
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._


// Used by all modules
val commonLibraries = Seq(
  "com.typesafe.scala-logging"  %%  "scala-logging"   % "3.1.0",
  "ch.qos.logback"              %   "logback-classic" % "1.1.3"
)

val proximityDisplayLibraries = {
  val pi4jVersion = "1.0"
  Seq(
    "com.pi4j"         %  "pi4j-core"   % pi4jVersion,
    "com.pi4j"         %  "pi4j-device" % pi4jVersion,
    "io.reactivex"     %% "rxscala"     % "0.26.0",
    "com.github.scopt" %% "scopt"       % "3.3.0",
    "org.scalaz"       %% "scalaz-core" % "7.2.0"
  )
}

libraryDependencies ++= (proximityDisplayLibraries ++ commonLibraries)


lazy val sharedProjectSettings = Seq(
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-optimise",
    "-language:postfixOps",
    "-language:implicitConversions"
  )
)



val depsJarName = "proximity-display-assembly-1.0-deps.jar"

val jarPath = "deployment"

val localJarDir = file(jarPath)

val piPath = "/home/pi/proximity"

val mainClassName = "com.devscala.explorer.ProximityApp"


// sbt-assembly: https://github.com/sbt/sbt-assembly
val assemblySettings = Seq(
  assemblyJarName in assembly := "proximity.jar",
  target          in assembly := localJarDir,
  mainClass       in assembly := Some(mainClassName),
  assemblyOption  in assembly := (assemblyOption in assembly).value.copy(includeScala = false, includeDependency = false),
  logLevel        in assembly := Level.Info,
  assemblyMergeStrategy in assembly := {
    case x if Set(".md", ".txt").exists(x.toLowerCase.endsWith) => MergeStrategy.discard
    case "logback.xml" => MergeStrategy.discard
    case x => (assemblyMergeStrategy in assembly).value(x)
  }
)

// sbt-deploy-ssh: https://github.com/shmishleniy/sbt-deploy-ssh
val deploymentSettings = Seq(
  deployArtifacts           ++= Seq(ArtifactSSH(localJarDir, piPath)),
  deployResourceConfigFiles ++= Seq("project/deploy.conf"),
  deploySshExecBefore       ++= Seq(
    (ssh: SSH) => {
      ssh.execOnce(s"""sudo pkill -f "$mainClassName"""")
      ssh.execOnce(s"rm $piPath/proximity.jar")
    }
  )
)


val `proximity-display` = (project in file("."))
  .settings(sharedProjectSettings: _*)
  .settings(assemblySettings: _*)
  .settings(deploymentSettings: _*)
  .settings(
    resolvers += Resolver.sonatypeRepo("public"),
    name      := "proximity-display",
    version   := "1.0"
  )

enablePlugins(DeploySSH)




TaskKey[Unit]("delDepsJar") := new File(s"$jarPath/$depsJarName").delete()

TaskKey[Unit]("copyLogbackCfg") := IO.copyFile(new File("src/main/resources/logback.xml"), new File("deployment/logback.xml"), preserveLastModified = false)



addCommandAlias("c",  "compile")

addCommandAlias("cc", "; clean; compile")

addCommandAlias("b",  "; assembly") // build

addCommandAlias("bf", "; b; assemblyPackageDependency") // buildFull

addCommandAlias("d",  "; b; delDepsJar; deploySsh pi") // deploy

addCommandAlias("df", "; bf; copyLogbackCfg ;deploySsh pi") // deployFull

