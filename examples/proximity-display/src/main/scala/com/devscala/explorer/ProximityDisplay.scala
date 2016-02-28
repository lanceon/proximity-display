package com.devscala.explorer

import com.devscala.explorer.distance.DistanceSensorApi
import com.devscala.explorer.lcd.TextDisplay
import com.typesafe.scalalogging.StrictLogging

/**
  * Created by AZ on 12.02.2016.
  */
class ProximityDisplay(lcd: TextDisplay, sensor: DistanceSensorApi) extends StrictLogging {

  def start(): Unit = {
    logger.info("ProximityDisplay: started")
    lcd.clear()
    lcd.write(0, "Distance:")
    sensor.distance.distinctUntilChanged throttleLast lcd.throttle foreach { d =>
      val text = if (d == 0) "N/A   " else s"$d cm  "
      lcd.write(1, text)
      logger.info(s"Updating LCD: $text")
    }
    sensor.diff collect { case d if math.abs(d.diff) > 10 => logger.info(s"Big move: $d") }
  }

  def stop(): Unit = {
    sensor.shutdown()
    lcd.clear()
    lcd.write(0, "ProximityDisplay")
    lcd.write(1, "Thank you. Bye!")
    logger.info("ProximityDisplay: finished")
  }

}
