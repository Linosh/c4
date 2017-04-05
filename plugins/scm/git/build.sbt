

lazy val baseSettings = Seq(
  version := "0.1",
  organization := "",
  name := "gitScm",
  scalaVersion := "2.12.1",
  packageOptions := Seq(ManifestAttributes(("PluginClass", "GitScm"))))

val root = (project in file("."))
  .settings(baseSettings)