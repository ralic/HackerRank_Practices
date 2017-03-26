package org.raliclo.HackerRank_Competitions.Contests.C101Hack47

// https://www.hackerrank.com/contests/101hack47/challenges/modular-game-of-numbers
import java.io.PrintWriter
import java.util.Scanner

/**
  * Created by raliclo on 21/03/2017.
  */
object ModularGameOfNumbers_scala {
  def main(args: Array[String]) {
    val sc: Scanner = new Scanner(System.in)
    val out: PrintWriter = new PrintWriter(System.out)
    val n: Int = sc.nextInt
    val x: Int = sc.nextInt
    val y: Int = sc.nextInt
    val a: Array[Int] = new Array[Int](x)
    val b: Array[Int] = new Array[Int](y)
    var i: Int = 0
    while (i < x) {
      a(i) = sc.nextInt
      i += 1;
      i - 1

    }
    i = 0
    while (i < y) {
      b(i) = sc.nextInt
      i += 1;
      i - 1

    }
    val cnt: Array[Int] = new Array[Int](n)
    i = 0
    while (i < x) {
      var j: Int = 0
      while (j < y) {
        cnt((a(i) + b(j)) % n) += 1;
        cnt((a(i) + b(j)) % n) - 1
        j += 1;
        j - 1
      }
      i += 1;
      i - 1
    }
    var min: Int = x + y + 3
    var res: Int = 0
    i = n - 1
    while (i >= 0) {

      if (cnt(i) < min) {
        min = cnt(i)
        res = n - i
      }
      i -= 1;
      i + 1
    }
    out.println(res)
    out.flush()
    out.close()
  }
}