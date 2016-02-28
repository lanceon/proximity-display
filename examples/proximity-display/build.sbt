import java.io.File

import deployssh.DeploySSH.ArtifactSSH
import deployssh.DeploySSH.Keys._
import jassh.SSH
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._

import Common._


val depsJarName = taskKey[String]("")

depsJarName := s"${name.value}-assembly-${version.value}-deps.jar"


val deploymentDir = file("examples/proximity-display/deployment")

val piPath = "/home/pi/proximity"

val mainClassName = "com.devscala.explorer.ProximityApp"


// sbt-assembly: https://github.com/sbt/sbt-assembly
val assemblySettings = Seq(
  //assemblyJarName in assembly := "proximity.jar",
  target          in assembly := deploymentDir,
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
  deployArtifacts           ++= Seq(ArtifactSSH(deploymentDir, piPath)),
  deployResourceConfigFiles ++= Seq("examples/proximity-display/project/deploy.conf"),
  deploySshExecBefore       ++= Seq(
    (ssh: SSH) => {
      ssh.execOnce(s"""sudo pkill -f "$mainClassName"""")
      ssh.execOnce(s"rm $piPath/${(assemblyJarName in assembly).value}")
    }
  ),
  deploySshExecAfter ++= Seq(
    (ssh: SSH) => {
      ssh.execOnce(s"ln -s $piPath/${(assemblyJarName in assembly).value} $piPath/proximity.jar")
      ssh.execOnce(s"ln -s $piPath/${depsJarName.value} $piPath/proximity-deps.jar")
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
    libraryDependencies ++= Seq(
      // modules
      "com.devscala.explorer" %% "lcd"      % "1.1-SNAPSHOT",
      "com.devscala.explorer" %% "distance" % "1.1-SNAPSHOT",
      // external deps
      "com.github.scopt" %% "scopt"       % "3.3.0",
      "org.scalaz"       %% "scalaz-core" % "7.2.0"
    )
  )


enablePlugins(DeploySSH)


TaskKey[Unit]("delDepsJar") := file(s"$deploymentDir/${depsJarName}").delete()

TaskKey[Unit]("copyLogbackCfg") := IO.copyFile(
  new File("examples/proximity-display/src/main/resources/logback.xml"),
  new File("examples/proximity-display/deployment/logback.xml"),
  preserveLastModified = false
)



addCommandAlias("c",  "compile")

addCommandAlias("cc", "; clean; compile")

addCommandAlias("b",  "; assembly") // build

addCommandAlias("bf", "; b; assemblyPackageDependency") // buildFull

addCommandAlias("d",  "; b; delDepsJar; deploySsh pi") // deploy

addCommandAlias("df", "; bf; copyLogbackCfg ;deploySsh pi") // deployFull

