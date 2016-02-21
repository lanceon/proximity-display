package com.devscala.explorer.distance

import com.devscala.explorer.distance.DistanceSensorApi.DistDiff
import rx.lang.scala.Observable

/**
  * Created by AZ on 06.02.2016.
  */
trait DistanceSensorApi {

  // single reads
  def readSensor: Int
  def readDistance: Int

  // stream of raw values from sensor
  def unstableSensor: Observable[Int]

  // stream of stabilized raw values from sensor
  def sensor: Observable[Int]

  // stream of stabilized distance values in cm
  def distance: Observable[Int]

  // stream of distance changes (positive/negative)
  def diff: Observable[DistDiff]

  def lastSensor: Int
  def lastDistance: Int

  // call to stop sensor value readings stream
  def shutdown(): Unit

}


object DistanceSensorApi {

  case class DistDiff(dist: Int, old: Int, diff: Int) {
    override def toString = s"DistDiff(dist=$dist, old=$old, diff=$diff)"
  }

}

