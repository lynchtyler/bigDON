// scalastyle:off

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

lazy val root =
  (project in file(".")).aggregate(
    wordStorm
  )

lazy val commonSettings = Seq(
  organization := "com.flashboomlet",
  scalaVersion := "2.11.7",
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  libraryDependencies ++= Seq(
    "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.1.2",
    "org.scalaz" % "scalaz-core_2.11" % "7.2.2"
  )
)

lazy val wordStorm = (project in file ("wordStorm"))
  .settings(commonSettings: _*)
  .settings(
    name := "wordStorm",
    version := "0.0.0"
  )

lazy val dataScavenger = (project in file ("dataScavenger"))
  .settings(commonSettings: _*)
  .settings(
    name := "dataScavenger",
    version := "0.0.0"
  )
