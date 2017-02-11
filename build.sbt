lazy val commonSettings = Seq(
  organization := "com.c4",
  version := "0.0.1",
  scalaVersion := "2.12.1"
)

lazy val testDependecies = Seq(
  "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(name := "core")
  .settings(libraryDependencies ++= testDependecies)



coverageEnabled := true
coverageMinimum := 100
coverageFailOnMinimum := true