package com.devscala.explorer.util

/**
  * Created by AZ on 02.02.2016.
  */
object Maths {

  /**
    * Interpolated function value for x argument by two known points: (x0, y0), (x1, y1)
    * https://en.wikipedia.org/wiki/Linear_interpolation
    */
  def linInt(x: Int, x0: Int, y0: Int, x1: Int, y1: Int): Int = {
    y0 + (y1 - y0) * (x -x0) / (x1 - x0)
  }

  def avg(seq: Iterable[Int]): Int = math.round(seq.sum.toDouble / seq.size).toInt

}
