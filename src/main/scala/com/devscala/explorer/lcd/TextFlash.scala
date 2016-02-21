package com.devscala.explorer.lcd

import com.pi4j.component.lcd.impl.GpioLcdDisplay

/**
  * Created by AZ on 24.01.2016.
  * new TextFlash(lcd).run()
  */
class TextFlash(lcd: GpioLcdDisplay) {

  val text1 = "Lorem ipsum dolr"
  val text2 = "Sed dapibus turp"

  type ms = Int

  def run(totalFlashTime: ms = 5000,
          minDelay: ms = 50,
          maxDelay: ms = 1000,
          delayStep: ms = 50): Unit = {

    (minDelay to maxDelay by delayStep) foreach { delay =>

      val flashes = totalFlashTime / delay
      lcd.write(0, s"Delay: $delay ms  ")

      (0 to flashes) foreach { step =>

        lcd.write(1, text1)
        Thread.sleep(delay)

        lcd.write(1, text2)
        Thread.sleep(delay)

      }

    }

  }

}
