//https://www.hackerrank.com/challenges/ctci-array-left-rotation
//Emmanuel T Odeke @ github

package main

import (
	"bufio"
	"fmt"
	"io"
	"log"
	"os"
	"strconv"
	"strings"
	"time"
)

func main() {
	// Performance Timer

	start := time.Now()
	// Other Code
	br := bufio.NewReader(os.Stdin)
	var lines []string
	for {
		line, err := br.ReadString('\n')
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Fatal(err)
		}
		lines = append(lines, line)
	}
	if len(lines) < 2 {
		log.Fatalf("expecting input of the form:\n\t<n> <rotations>\n\t<values...>")
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

	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("Time Elapsed:", elapsed)
}

func rotate(test []string) []string {
	//fmt.Println(test)
	var swap = test[0]
	//fmt.Println(len(test))
	for i := 1; i < len(test); i++ {
		test[i - 1] = test[i]
	}
	test[len(test) - 1] = swap
	return test
}