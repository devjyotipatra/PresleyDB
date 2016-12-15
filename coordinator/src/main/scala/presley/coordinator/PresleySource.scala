package presley.coordinator

import akka.actor.Actor
import com.typesafe.scalalogging.slf4j.StrictLogging

import scala.concurrent.duration.FiniteDuration

import scala.concurrent.duration._

/**
  * Created by 17349 on 15/12/16.
  */
object PresleySource {
    case object start
}


trait PresleySource extends Actor with StrictLogging {
  import PresleySource._

  def maxUnackedBatches: Int

  def waitingPeriod: FiniteDuration = 5.seconds


}
