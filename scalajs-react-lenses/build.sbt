lazy val root = (project in file(".")).
  enablePlugins(ScalaJSPlugin).
  settings(
    name:= "scalajs-react-lenses-blog",
    version:="1.0",
    scalaVersion := "2.11.6",

    resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",


    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "0.9.0",
    libraryDependencies += "com.github.japgolly.scalajs-react" %%% "extra" % "0.9.0",

    libraryDependencies += "com.github.japgolly.scalacss" %%% "ext-react" % "0.2.0"
  )
