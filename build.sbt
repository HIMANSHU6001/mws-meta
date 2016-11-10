name := """MWS-Books"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  // Play
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  // DB
  "com.h2database" % "h2" % "1.4.187",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc4",
  // Authentication
  "jp.t2v" %% "play2-auth"        % "0.14.2",
  "jp.t2v" %% "play2-auth-social" % "0.14.2",
  play.sbt.Play.autoImport.cache,
  // WebJars (i.e. client-side) dependencies
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "bootstrap" % "3.3.6" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.5.5" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-bootstrap" % "1.3.2" exclude("org.webjars", "jquery"),
  // Testing
  specs2 % Test
)

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Atlassian Releases" at "https://maven.atlassian.com/public/"
)

fork in run := false
