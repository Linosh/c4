lazy val commonSettings = Seq(
  organization := "com.c4",
  version := "0.0.1",
  scalaVersion := "2.11.7"
)

lazy val testDependecies = Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(name := "core")
  .settings(libraryDependencies ++= testDependecies)



coverageEnabled := true
coverageMinimum := 100
coverageFailOnMinimum := true