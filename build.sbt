name := """silence.cloud"""

version := "1.0.15-SNAPSHOT"

lazy val core = (project in file("modules/core")).enablePlugins(PlayJava, PlayEbean)

lazy val emails = (project in file("modules/emails")).enablePlugins(PlayJava, PlayEbean)

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean).dependsOn(core, emails).aggregate(core, emails)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += cacheApi
libraryDependencies += jcache
libraryDependencies += javaForms
libraryDependencies += javaJpa
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.2.12.Final"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.8-dmr"
libraryDependencies += "javax.mail" % "javax.mail-api" % "1.6.0"

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
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=3333",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)
