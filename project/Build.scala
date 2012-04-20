  import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "snaps"
    val appVersion      = "0.3-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      //"ua.t3hnar.bcrypt" % "scala-bcrypt" % "10.4.1.3"
      "org.mindrot" % "jbcrypt" % "0.3m"

    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
