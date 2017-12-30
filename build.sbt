name := """silence.cloud"""

version := "1.0.3-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.2.12.Final"

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.194"
libraryDependencies += "org.flywaydb" %% "flyway-play" % "4.0.0"
libraryDependencies += evolutions
// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.13.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

PlayKeys.externalizeResources := false

playEbeanModels in Compile := Seq("models.*")
playEbeanDebugLevel := 4
playEbeanAgentArgs += ("detect" -> "false")
inConfig(Test)(PlayEbean.scopedSettings)

playEbeanModels in Test := Seq("models.*")

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)
