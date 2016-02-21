package com.devscala.explorer

import scopt.{Read, OptionParser}

import scala.collection.immutable.ListMap

/**
  * Created by AZ on 27.01.2016.
  */
case class CmdLineParams(delayBetweenBursts:   Int = 1000,
                         delayBetweenReadings: Int = 50,
                         burstSize:            Int = 10,
                         displayThrottle:      Int = 500,
                         pins:                 Map[String,String] = ListMap("rs" -> "1" , "en" -> "4", "d4" -> "5", "d5" -> "6", "d6" -> "7", "d7" -> "2")) {

  override def toString =
    getClass.getDeclaredFields.filterNot(_.isSynthetic).map{ f =>
      f.setAccessible(true)
      s"${f.getName} = ${f.get(this)}"
    }.mkString(", ")

}

object CmdLineParams {

  def parser = new OptionParser[CmdLineParams]("proximity.jar") {

    // Options: https://github.com/scopt/scopt

    val defPins = Default.pins.map{ case (k,v) => s"$k=$v" }.mkString(",")

    implicit def mapRead[K: Read, V: Read]: Read[ListMap[K,V]] = Read.reads { (s: String) =>
      ListMap[K,V]() ++ s.split(Read.sep).map(implicitly[Read[(K,V)]].reads).toMap
    }

    opt[ListMap[String,String]]("pins") valueName defPins action { (x, c) =>
      c.copy(pins = x) } text s"LCD pin configuration, wiringPi numbering scheme (default: $defPins)"

    opt[Int]('d', "delayBetweenBursts") action { (x, c) =>
      c.copy(delayBetweenBursts = x) } text s"delay between sensor burst reads, ms (default: ${Default.delayBetweenBursts})"

    opt[Int]('r', "delayBetweenReadings") action { (x, c) =>
      c.copy(delayBetweenReadings = x) } text s"delay between sensor reads within bursts, ms (default: ${Default.delayBetweenReadings})"

    opt[Int]('b', "burstSize") action { (x, c) =>
      c.copy(burstSize = x) } text s"number of sensor reads within bursts (default: ${Default.burstSize})"

    opt[Int]('t', "displayThrottle") action { (x, c) =>
      c.copy(displayThrottle = x) } text s"min delay between subsequent LCD updates, ms (default: ${Default.displayThrottle})"

    help("help") text "prints this usage text"

  }

  lazy val Default = CmdLineParams()

}