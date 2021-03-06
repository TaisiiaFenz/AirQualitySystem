name := "AirQualitySystemWeb"


version := "1.0"

lazy val `AirQualitySystemWeb` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
//libraryDependencies ++= Seq(
//  javaJdbc
//)
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.46"
)
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test +=  baseDirectory.value / "app/test"

libraryDependencies += guice
//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
fork := true // required for "sbt run" to pick up javaOptions
val akkaVersion = "2.6.1"
libraryDependencies += ws
libraryDependencies += "org.webjars" % "flot" % "0.8.3-1"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.6"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test

libraryDependencies += "io.circe" %% "circe-core" % "0.11.1"
libraryDependencies += "io.circe" %% "circe-generic" % "0.11.1"
libraryDependencies += "io.circe" %% "circe-parser" % "0.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
)

libraryDependencies += "org.springframework" % "spring-beans" % "5.2.2.RELEASE"


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"

