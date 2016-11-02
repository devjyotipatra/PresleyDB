name := "PresleyDB"

version := "1.0"



val mySettings = Seq(
  organization := "com.myntra",
  scalaVersion := "2.11.8",
  parallelExecution in Test := false,
  fork in Test := true,
  resolvers ++= extraRepos,
  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) })



lazy val core = (project in file("core"))
  .settings(mySettings:_*)
  .settings(name := "presley-core")
  .settings(scalacOptions += "-language:postfixOps")
  .settings(libraryDependencies ++= coreDeps)



lazy val extraRepos = Seq(
  "twitter-repo" at "http://maven.twttr.com",
  "Pellucid Bintray" at "http://dl.bintray.com/pellucid/maven",
  "spray repo" at "http://repo.spray.io"
)




lazy val commonDeps = Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

lazy val coreDeps = commonDeps ++ Seq(
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "org.scodec" %% "scodec-bits" % "1.0.10"
)

