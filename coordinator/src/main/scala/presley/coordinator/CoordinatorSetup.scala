package main.scala.presley.coordinator


import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by 17349 on 15/12/16.
  */
object GlobalConfig {

  /*
    Loads the overall configuration in a specific order:
    - System properties
    - Config file in location specified by presley.coordinator.config.file
    - presley-coordinator-defaults.conf (resource / in jar)
   */

  val systemConfig: Config = {
      val config = sys.props.get("presley.coordinator.config.file")
                          .map { path => ConfigFactory.parseFile(new java.io.File(path)) }
                          .getOrElse(ConfigFactory.empty)

      ConfigFactory.defaultOverrides.withFallback(config)
        .withFallback(ConfigFactory.parseURL(getClass.getResource("/presley-defaults.conf")))
        .resolve()
  }

  trait CoordinatorConfig {
    def system: ActorSystem

    def config: Config

    lazy val systemConfig: Config = GlobalConfig.systemConfig
  }


}
