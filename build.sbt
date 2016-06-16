// scalastyle:off

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

mainClass in (Compile, run) := Some("com.flashboomlet.fetching.Driver")

lazy val root =
  (project in file(".")).aggregate(
    wordStorm,
    dataScavenger
  )

lazy val commonSettings = Seq(
  organization := "com.flashboomlet",
  scalaVersion := "2.11.8",
  resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Typesafe Releases" at "https://repo.typesafe.com/typesafe/maven-releases/",
    "Maven central" at "http://repo1.maven.org/maven2/"
  ),
  libraryDependencies ++= Seq(
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.4",
    "org.scalaz" %% "scalaz-core" % "7.2.2",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.jsoup" % "jsoup" % "1.9.2",
    "com.danielasfregola" %% "twitter4s" % "0.1",
    "com.typesafe" % "config" % "1.3.0",
    "org.reactivemongo" %% "reactivemongo" % "0.11.13",
    "ch.qos.logback"  %  "logback-classic" % "1.1.3",
    "org.slf4j" %  "slf4j-api" % "1.7.14",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
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
    version := "0.0.0")
  .dependsOn(wordStorm)
