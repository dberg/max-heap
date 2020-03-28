import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.cybergstudios"
ThisBuild / organizationName := "cybergstudios"

lazy val root = (project in file("."))
  .settings(
    name := "Max Heap",
    libraryDependencies += scalaTest % Test
  )
