
name := "mongorepo"

version := "0.1"

scalaVersion := "2.12.5"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  guice,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.6-play26",
  "com.typesafe.play" %% "play-json" % "2.6.7",
  specs2 % Test
)