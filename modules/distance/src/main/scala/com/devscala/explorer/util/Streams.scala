package com.devscala.explorer.util

import rx.lang.scala.Observable

/**
  * Created by AZ on 04.02.2016.
  */
object Streams {

  implicit class AvgStream(val in: Observable[Int]) extends AnyVal {

    def floatingWindowAvg(window: Int): Observable[Int] = in.slidingBuffer(window, 1).map { seq =>
      math.round(seq.sum.toDouble / seq.length).toInt
    }

    /**
      * @return (Average, Max diff from value to average, Average of all diffs)
      */
    def floatingWindow(window: Int): Observable[(Double, Double, Double)] = in.slidingBuffer(window, 1).map { seq =>
      if (seq.nonEmpty) {
        val sum = seq.sum.toDouble
        val avg = sum / seq.length
        val diffs = seq.map(v => math.abs(avg - v))
        //println(s"floating: seq = $seq, avg = $avg, diffs = $diffs") // debug log
        (avg, diffs.max, diffs.sum/diffs.length)
      } else (0d, 0d, 0d)
    }

  }



}
