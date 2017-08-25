val commonSettings = Seq(
  organization := "com.c4",
  name := "core",
  version := "0.0.1",
  scalaVersion := "2.12.1"
)

val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.3"
val scalaTestVersion = "3.0.1"
val mockitoVersion = "2.7.19"

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion)

val testDependecies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "org.mockito" % "mockito-core" % mockitoVersion
).map(_ % "test")

val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= akkaDependencies ++ testDependecies)


coverageEnabled := true
coverageMinimum := 10
coverageFailOnMinimum := true
coverageExcludedPackages := ".*AkkaHttpApi.*"