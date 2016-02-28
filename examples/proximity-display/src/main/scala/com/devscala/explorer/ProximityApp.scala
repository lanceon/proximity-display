package com.devscala.explorer

import com.devscala.explorer.distance.{DistanceSensor, DistanceSensorApi, UnfilteredCalibration}
import com.devscala.explorer.lcd.TextDisplay
import com.pi4j.gpio.extension.mcp.{MCP3008GpioProvider, MCP3008Pin}
import com.pi4j.io.gpio.{Pin, RaspiPin}
import com.pi4j.io.spi.SpiChannel
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.duration._
import scalaz.{-\/, \/, \/-}

/**
  * Created by AZ on 18.01.2016.
  */
object ProximityApp extends StrictLogging {

  def main(args: Array[String]) {

    val proximityDisplay = for {
      params <- parseParams(args)
      _ <- \/-(initApp(params))
      hw <- initHardware(params)
    } yield new ProximityDisplay(hw._1, hw._2)

    proximityDisplay match {
      case \/-(display) =>
        display.start()
        scala.io.StdIn.readLine()
        display.stop()
      case -\/(error) =>
        println(s"Error: $error")
    }

  }


  private def parseParams(args: Array[String]) = {
    import CmdLineParams._
    parser.parse(args, Default) map \/.right getOrElse \/.left("Invalid command line parameters")
  }

  private def initApp(params: CmdLineParams): Unit = {

    println(
      s"""
         |Proximity Display parameters: $params
         |Start with --help for details
         |Press Enter to exit
      """.stripMargin)

  }

  private def initHardware(params: CmdLineParams) = {

    \/.fromTryCatchThrowable[(TextDisplay, DistanceSensorApi), Throwable] {

      val sensorPin = MCP3008Pin.CH0
      val mcp = new MCP3008GpioProvider(SpiChannel.CS0)
      logger.info(s"ADC is ready")

      def pinByName(name: String): Pin = {
        val gpioNum = params.pins.getOrElse(name, CmdLineParams.Default.pins(name))
        RaspiPin.getPinByName("GPIO " + gpioNum)
      }

      val lcd = TextDisplay(params.displayThrottle.millis, pinByName("rs"), pinByName("en"),
        pinByName("d4"), pinByName("d5"), pinByName("d6"), pinByName("d7"))
      logger.info("LCD is ready")

      val sensor = new DistanceSensor(mcp, sensorPin, UnfilteredCalibration, params.delayBetweenBursts.millis,
        params.delayBetweenReadings.millis, params.burstSize)
      logger.info("Sensor object is ready")

      (lcd, sensor)

    } leftMap (_.getMessage)

  }

}
