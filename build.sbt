val commonSettings = Seq(
  organization := "com.c4",
  version := "0.0.1",
  scalaVersion := "2.12.1"
)

val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.3"
val scalaTestVersion = "3.0.1"

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion)

val testDependecies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(name := "core")
  .settings(libraryDependencies ++= akkaDependencies ++ testDependecies)


coverageEnabled := true
coverageMinimum := 100
coverageFailOnMinimum := true