name := "tp-scala-json"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies in ThisBuild += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

lazy val tpPlayJson = (project in file("tp/playjson"))
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.4.2"
    )
  )

lazy val tpCirce = (project in file("tp/circe"))
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.4.0",
      "io.circe" %% "circe-generic" % "0.4.0",
      "io.circe" %% "circe-parser" % "0.4.0"
    )
  )
