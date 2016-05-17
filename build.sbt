ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

lazy val root =
  (project in file(".")).aggregate(
    wordStorm
  )

lazy val commonSettings = Seq(
  organization := "com.flashboomlet",
  scalaVersion := "2.11.7"
)

lazy val wordStorm = (project in file ("wordStorm"))
  .settings(commonSettings: _*)
  .settings(
    name := "wordStorm",
    version := "0.1.0"
  )