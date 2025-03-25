name := "consumer_http"

version := "0.1"

scalaVersion := "2.12.7"

val akkaVersion = "2.5.13"


libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-stream-kafka" % "1.1.0",
  "org.apache.kafka" % "kafka-clients" % "2.3.1",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-http"    % "10.0.11",
  "com.typesafe.play" %% "play-json" % "2.6.10"
)

// SLF4J Simple Logger (for simple console output)
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.36"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.4"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"

mainClass in assembly := Some("app.HttpConsumer")

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
