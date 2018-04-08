name := """play-getting-started"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  ws
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )
libraryDependencies += json
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.8.0"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.2"