package com.devscala.explorer.distance

import com.devscala.explorer.distance.DistanceSensorApi.DistDiff
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider
import com.pi4j.io.gpio.Pin
import com.typesafe.scalalogging.StrictLogging
import rx.lang.scala.Observable
import rx.lang.scala.subjects.PublishSubject

import scala.concurrent.duration._

/**
  * Created by AZ on 25.01.2016.
  */
class DistanceSensor(mcp:                  MCP3008GpioProvider,
                     sensorPin:            Pin,
                     cal:                  DistanceCalibration,
                     delayBetweenBursts:   FiniteDuration,
                     delayBetweenReadings: FiniteDuration,
                     burstSize:            Int) extends DistanceSensorApi with StrictLogging {

  protected lazy val stopped = PublishSubject[Unit]

  protected lazy val timer = Observable interval delayBetweenBursts takeUntil stopped

  override def readSensor: Int = cal.sensorFilter(math.round(mcp.getValue(sensorPin)).toInt)
  override def readDistance: Int = cal.distanceFilter(cal.toDistance(readSensor))

  override lazy val unstableSensor: Observable[Int] = timer map { _ => readSensor }

  override lazy val sensor = Observable.just(0) ++ timer flatMap{ t =>
    logger.debug(s"Burst $t")
    val readings = Observable interval delayBetweenReadings take burstSize map { _ => readSensor }
    val avg = for ((finalSum, finalCount) <- readings.foldLeft((0d, 0)){
      case ((sum, count), elem) => (sum + elem, count + 1)
    }) yield finalSum / finalCount
    avg.map(v => { val x = math.round(v).toInt; logger.debug(s"Burst avg: $v"); x })
  }

  override lazy val distance = sensor map cal.toDistance

  override lazy val diff = distance drop 1 zip distance collect { case (newVal, oldVal) if newVal != oldVal =>
    DistDiff(newVal, oldVal, newVal - oldVal)
  }

  override def lastSensor = sensor.last.toBlocking.headOption getOrElse 0
  override def lastDistance = distance.last.toBlocking.headOption getOrElse 0

  override def shutdown() = stopped.onNext(())

}
