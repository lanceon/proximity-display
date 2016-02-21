package com.devscala.explorer.distance

import java.io.{FileWriter, BufferedWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
  * Created by AZ on 18.01.2016.
  */
class DistanceSensorData(sensor: DistanceSensor,
                         outputFilename: String = s"sensor_data_${LocalDateTime.now.format(DateTimeFormatter.ISO_TIME).take(5).replace(":", "")}.txt") {

  def run(): Unit = {

    val start = System.currentTimeMillis()
    val out = new BufferedWriter(new FileWriter(outputFilename), 1024*1024)

    sensor.distance.foreach{ d =>
      val m = s"${System.currentTimeMillis() - start}, $d\n"
      print(m)
      out.write(m)
      out.flush()
    }

    out.flush()
    out.close()

  }

}
