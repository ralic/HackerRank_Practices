//https://www.hackerrank.com/challenges/ctci-array-left-rotation
package main

import (
	"bufio"
	"os"
	"fmt"
	"strings"
	"strconv"
)

func main() {
	// Performance Timer


	//start := time.Now()
	// Other Code
	scanner := bufio.NewScanner(os.Stdin)
	var lines []string
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	line1 := strings.Split(lines[0], " ")
	line2 := strings.Split(lines[1], " ")
	//fmt.Println(line1)
	fmt.Println(line2)
	repeats, _ := strconv.Atoi(line1[1])

	for i := 0; i < repeats; i++ {
		rotate(line2)
		if i == (repeats - 1) {
			//fmt.Println(line2)
			for j := 0; j < len(line2); j++ {
				fmt.Print(line2[j] + " ")
			}
		}
	}

	if err := scanner.Err(); err != nil {
		fmt.Fprintln(os.Stderr, "reading standard input:", err)
	}


	// Performance Report
	//elapsed := time.Since(start)
	//fmt.Println("Time Elapsed:", elapsed)
}

func rotate(test []string) []string {
	//fmt.Println(test)
	var swap = test[0];
	//fmt.Println(len(test))
	for i := 1; i < len(test); i++ {
		test[i - 1] = test[i]
	}
	test[len(test) - 1] = swap
	return test
}
