//https://www.hackerrank.com/challenges/ctci-array-left-rotation
package main

import (
	"bufio"
	"os"
	"fmt"
	"strings"
	"time"
)

var line1 []string

func main() {
	// Performance Timer
	start := time.Now()
	// Other Code
	scanner := bufio.NewScanner(os.Stdin)
	var i int = 0

	for scanner.Scan() {
		//var line1 []string
		if (i == 0) {
			line1 := strings.Split(scanner.Text(), " ")
			fmt.Println(line1)
		}
		if (i == 1 ) {
			break
		}
		i++
	}

	if err := scanner.Err(); err != nil {
		fmt.Fprintln(os.Stderr, "reading standard input:", err)
	}


	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("Time Elapsed:", elapsed)
}

func rotate(test []string) []string {
	swap := test[0];
	for i := 1; i < len(test) - 1; i++ {
		test[i - 1] = test[i]
	}
	test[len(test)] = swap
	return test
}
