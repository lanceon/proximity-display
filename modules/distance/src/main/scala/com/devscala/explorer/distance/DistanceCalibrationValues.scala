package com.devscala.explorer.distance

trait DistanceCalibrationValues {
  this: DistanceCalibration =>

  // pairs: (sensor value, distance in cm)
  override protected lazy val measuredValues = Array(
    (93,  90),
    (100, 85),
    (106, 80),
    (111, 75),
    (124, 70),
    (135, 65),
    (142, 60),
    (154, 55),
    (175, 50),
    (192, 45),
    (216, 40),
    (245, 35),
    (283, 30),
    (337, 25),
    (418, 20),
    (550, 15),
    (816, 10),
    (914, 5)
  )

}


object UnfilteredCalibration extends DistanceCalibration with DistanceCalibrationValues


object FilteredCalibration extends DistanceCalibration with DistanceCalibrationValues {

  // detectable distance range, cm
  val MinSpecDistance = 10
  val MaxSpecDistance = 60 // 80

  // limits calculated from min/max of spec and experimental values
  lazy val MinSensorValue = measuredValues.min._1
  lazy val MaxSensorValue = measuredValues.max._1

  override def sensorFilter(value: Int): Int = {
    val v = super.sensorFilter(value)
    if (v >= MinSensorValue && v <= MaxSensorValue) v
    else 0
  }

  override def distanceFilter(value: Int): Int = {
    val v = super.distanceFilter(value)
    if (v >= MinSpecDistance && v <= MaxSpecDistance) v
    else 0
  }

}