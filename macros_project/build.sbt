name := "macros_project"

scalaVersion in ThisBuild := "2.11.8"

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers ++= Seq(
    "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
    "Atlassian Releases" at "https://maven.atlassian.com/public/",
    Resolver.url("scalameta", url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns)
  ),
  libraryDependencies ++= Seq(
    // Play
    "com.typesafe.play" %% "play-slick" % "2.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
    // DB
    "com.h2database" % "h2" % "1.4.187",
    "org.postgresql" % "postgresql" % "9.4-1206-jdbc4"
  ),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0.132" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in (Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  sources in (Compile, doc) := Nil // macroparadise doesn't work with scaladoc yet
)

lazy val macros = project.settings(
  metaMacroSettings,
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.3.0"
)

lazy val macros_project = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(metaMacroSettings)
  .aggregate(macros)
  .dependsOn(macros)

//mainClass in (Compile,run) := Some("Main")

fork in run := false
