import sbt.Keys.javaOptions

// scalastyle:off

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

mainClass in (Compile, run) := Some("com.flashboomlet.Driver")

lazy val root =
  (project in file(".")).aggregate(
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
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4.7",
    "org.reactivemongo" %% "reactivemongo" % "0.11.13",
    "ch.qos.logback"  %  "logback-classic" % "1.1.3",
    "org.slf4j" %  "slf4j-api" % "1.7.14",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"))

lazy val dataScavenger = (project in file ("dataScavenger"))
  .settings(commonSettings: _*)
  .settings(
    name := "dataScavenger",
    version := "0.1.0",
    javaOptions += "-Dlogback.configurationFile=../dataScavenger/src/main/resources/logback.xml",
    dockerfile in docker := {
      val artifact: File = assembly.value
      val artifactTargetPath = s"/opt/${artifact.name}"
      val runJar = Seq(
        "java",
        "-jar",
        "-Dlogback.configurationFile=/opt/conf/logback.xml",
        artifactTargetPath)

      new Dockerfile {
        from("java:8-jre")
        add(artifact, artifactTargetPath)
        add(new File("./dataScavenger/src/main/resources/logback.xml"), "/opt/conf/logback.xml")
        entryPoint(runJar: _*)
      }
    })
  .enablePlugins(DockerPlugin)
