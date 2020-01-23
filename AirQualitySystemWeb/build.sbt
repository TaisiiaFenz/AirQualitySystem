name := "AirQualitySystemWeb"
 
version := "1.0"

lazy val `AirQualitySystemWeb` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
libraryDependencies ++= Seq(
  javaJdbc
)
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.46"
)
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
fork := true // required for "sbt run" to pick up javaOptions
val akkaVersion = "2.5.25"
libraryDependencies += ws
libraryDependencies += "org.webjars" % "flot" % "0.8.3-1"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.6"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test