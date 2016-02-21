package com.devscala.explorer.lcd

import com.pi4j.component.lcd.impl.GpioLcdDisplay
import com.pi4j.io.gpio.Pin

import scala.concurrent.duration._

/**
  * Created by AZ on 14.02.2016.
  */
case class TextDisplay(throttle: Duration,
                       rs: Pin,
                       en: Pin,
                       d4: Pin,
                       d5: Pin,
                       d6: Pin,
                       d7: Pin) extends GpioLcdDisplay(2, 16, rs, en, d4, d5, d6, d7)
