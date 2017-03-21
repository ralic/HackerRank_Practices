package BenchmarkGame

/**
  * Created by raliclo on 8/24/16.
  * Project Name : TestNG-1
  */

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Date
import java.util.concurrent.CyclicBarrier


object SpectralNorm {
  private val formatter: NumberFormat = new DecimalFormat("#.000000000")

  def main(args: Array[String]) {
    var n: Int = 1000
    //        if (args.length > 0) n = Integer.parseInt(args[0]);
    n = 5500
    val begin: Date = new Date
    System.out.println(formatter.format(spectralnormGame(n)))
    System.out.println("Time Elapsed:" + (new Date().getTime - begin.getTime))
  }

  private def spectralnormGame(n: Int): Double = {
    // create unit vector
    val u: Array[Double] = new Array[Double](n)
    val v: Array[Double] = new Array[Double](n)
    val tmp: Array[Double] = new Array[Double](n)
    var i: Int = 0
    while (i < n) {
      u(i) = 1.0 {
        i += 1; i - 1
      }
    }
    // get available processor, then set up syn object
    val nthread: Int = Runtime.getRuntime.availableProcessors
    Approximate.barrier = new CyclicBarrier(nthread)
    val chunk: Int = n / nthread
    val ap: Array[SpectralNorm_scala.Approximate] = new Array[SpectralNorm_scala.Approximate](nthread)
    var i: Int = 0
    while (i < nthread) {
      {
        val r1: Int = i * chunk
        val r2: Int = if ((i < (nthread - 1))) r1 + chunk
        else n
        ap(i) = new SpectralNorm_scala.Approximate(u, v, tmp, r1, r2)
      }
      {
        i += 1; i - 1
      }
    }
    var vBv: Double = 0
    var vv: Double = 0
    var i: Int = 0
    while (i < nthread) {
      {
        try {
          ap(i).join()
          vBv += ap(i).m_vBv
          vv += ap(i).m_vv
        }
        catch {
          case e: Exception => {
            e.printStackTrace()
          }
        }
      }
      {
        i += 1; i - 1
      }
    }
    return Math.sqrt(vBv / vv)
  }

  private object Approximate {
    private val barrier: CyclicBarrier = null

    /* return element i,j of infinite matrix A */ private def eval_A(i: Int, j: Int): Double = {
      val div: Int = (((i + j) * (i + j + 1) >>> 1) + i + 1)
      return 1.0 / div
    }
  }

  private class Approximate(var _u: Array[Double], var _v: Array[Double], var _tmp: Array[Double], var range_begin: Int, var range_end: Int) extends Thread {
    start()
    private var m_vBv: Double = 0
    private var m_vv: Double = 0

    override def run() {
      // 20 steps of the power method
      var i: Int = 0
      while (i < 10) {
        {
          MultiplyAtAv(_u, _tmp, _v)
          MultiplyAtAv(_v, _tmp, _u)
        }
        {
          i += 1; i - 1
        }
      }
      var i: Int = range_begin
      while (i < range_end) {
        {
          m_vBv += _u(i) * _v(i)
          m_vv += _v(i) * _v(i)
        }
        {
          i += 1; i - 1
        }
      }
    }

    /* multiply vector v by matrix A, each thread evaluate its range only */ final private def MultiplyAv(v: Array[Double], Av: Array[Double]) {
      var i: Int = range_begin
      while (i < range_end) {
        {
          var sum: Double = 0
          var j: Int = 0
          while (j < v.length) {
            sum += Approximate.eval_A(i, j) * v(j) {
              j += 1; j - 1
            }
          }
          Av(i) = sum
        }
        {
          i += 1; i - 1
        }
      }
    }

    /* multiply vector v by matrix A transposed */ final private def MultiplyAtv(v: Array[Double], Atv: Array[Double]) {
      var i: Int = range_begin
      while (i < range_end) {
        {
          var sum: Double = 0
          var j: Int = 0
          while (j < v.length) {
            sum += Approximate.eval_A(j, i) * v(j) {
              j += 1; j - 1
            }
          }
          Atv(i) = sum
        }
        {
          i += 1; i - 1
        }
      }
    }

    /* multiply vector v by matrix A and then by matrix A transposed */ final private def MultiplyAtAv(v: Array[Double], tmp: Array[Double], AtAv: Array[Double]) {
      try {
        MultiplyAv(v, tmp)
        // all thread must syn at completion
        Approximate.barrier.await
        MultiplyAtv(tmp, AtAv)
        // all thread must syn at completion
        Approximate.barrier.await
      }
      catch {
        case e: Exception => {
          e.printStackTrace()
        }
      }
    }
  }

}