package BenchmarkGame

/**
  * Created by raliclo on 8/24/16.
  * Project Name : TestNG-1
  */

import java.io.{BufferedOutputStream, OutputStream}
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

object Mandelbrot_scala {
  private[BenchmarkGame] var out: Array[Array[Byte]] = null
  private[BenchmarkGame] var yCt: AtomicInteger = null
  private[BenchmarkGame] var Crb: Array[Double] = null
  private[BenchmarkGame] var Cib: Array[Double] = null

  private[BenchmarkGame] def getByte(x: Int, y: Int): Int = {
    var res: Int = 0
    var i: Int = 0
    while (i < 8) {
      {
        var Zr1: Double = Crb(x + i)
        var Zi1: Double = Cib(y)
        var Zr2: Double = Crb(x + i + 1)
        var Zi2: Double = Cib(y)
        var b: Int = 0
        var j: Int = 49
        do {
          {
            {
              val nZr1: Double = Zr1 * Zr1 - Zi1 * Zi1 + Crb(x + i)
              val nZi1: Double = Zr1 * Zi1 + Zr1 * Zi1 + Cib(y)
              Zr1 = nZr1
              Zi1 = nZi1
              val nZr2: Double = Zr2 * Zr2 - Zi2 * Zi2 + Crb(x + i + 1)
              val nZi2: Double = Zr2 * Zi2 + Zr2 * Zi2 + Cib(y)
              Zr2 = nZr2
              Zi2 = nZi2
              if (Zr1 * Zr1 + Zi1 * Zi1 > 4) {
                b |= 2
                if (b == 3) break //todo: break is not supported
              }
              if (Zr2 * Zr2 + Zi2 * Zi2 > 4) {
                b |= 1
                if (b == 3) break //todo: break is not supported
              }
            }
          }
        } while ( {
          j -= 1; j
        } > 0)
        res = (res << 2) + b
      }
      i += 2
    }
    return res ^ -1
  }

  private[BenchmarkGame] def putLine(y: Int, line: Array[Byte]) {
    var xb: Int = 0
    while (xb < line.length) {
      line(xb) = getByte(xb * 8, y).toByte {
        xb += 1; xb - 1
      }
    }
  }

  @throws[Exception]
  def main(args: Array[String]) {
    var N: Int = 16000
    if (args.length >= 1) N = args(0).toInt
    val begin: Date = new Date
    Crb = new Array[Double](N + 7)
    Cib = new Array[Double](N + 7)
    val invN: Double = 2.0 / N
    var i: Int = 0
    while (i < N) {
      {
        Cib(i) = i * invN - 1.0
        Crb(i) = i * invN - 1.5
      }
      {
        i += 1; i - 1
      }
    }
    yCt = new AtomicInteger
    out = new Array[Array[Byte]](N, (N + 7) / 8)
    val pool: Array[Thread] = new Array[Thread](2 * Runtime.getRuntime.availableProcessors)
    var i: Int = 0
    while (i < pool.length) {
      pool(i) = new Thread() {
        override def run() {
          var y: Int = 0
          while ((y = yCt.getAndIncrement) < out.length) {
            putLine(y, out(y))
          }
        }
      }
      {
        i += 1; i - 1
      }
    }
    for (t <- pool) t.start()
    for (t <- pool) t.join()
    val stream: OutputStream = new BufferedOutputStream(System.out)
    stream.write(("P4\n" + N + " " + N + "\n").getBytes)
    var i: Int = 0
    while (i < N) {
      stream.write(out(i)) {
        i += 1; i - 1
      }
    }
    stream.close()
    System.out.println("Time Elapsed:" + (new Date().getTime - begin.getTime))
  }
}