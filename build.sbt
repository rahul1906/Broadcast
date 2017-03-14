name := "Broadcast"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray" % "spray-json_2.11" % "1.3.3",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "mysql" % "mysql-connector-java" % "5.1.25",
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2" ,
    "com.maxmind.geoip2" % "geoip2" % "2.8.1"

  )
}
