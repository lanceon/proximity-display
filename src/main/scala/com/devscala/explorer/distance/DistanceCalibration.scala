package com.devscala.explorer.distance

import com.devscala.explorer.util.Maths
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by AZ on 25.01.2016.
  */
abstract class DistanceCalibration extends LazyLogging {

  // Experimental translation table from sensor values to distance (sensor value, distance in cm)
  protected def measuredValues: Array[(Int, Int)]

  val translation = (0 to 1023).map(lookupSensorValue).toArray

  protected def lookupSensorValue(value: Int): Int =
    if (value == 0) 0 else {
      val pairs: Array[((Int, Int), (Int, Int))] = measuredValues.zip(measuredValues.drop(1) :+ measuredValues.last)
      pairs.find { case ((v1, d1), (v2, d2)) => value >= v1 && value < v2 }
        .map { case ((v1, d1), (v2, d2)) => Maths.linInt(value, v1, d1, v2, d2) } getOrElse 0
    }

  def toDistance(value: Int): Int =
    if (value >=0 && value <= 1023) translation(value)
    else throw new IllegalArgumentException(s"Invalid distance sensor value: $value")

  // filter sensor input data
  def sensorFilter(value: Int) = value

  // filter output distance data
  def distanceFilter(value: Int) = value

}

