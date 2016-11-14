//https://www.hackerrank.com/challenges/ctci-array-left-rotation
package main

import (
	"fmt"
	"log"
	"time"
)

func main() {
	// Performance Timer


	start := time.Now()
	// Other Code
	var words, repeats int
	if _, err := fmt.Scan(&words, &repeats); err != nil {
		log.Print("  Scan for i failed, due to ", err)
		return
	}

	data := make([]int, words)

	for i := 0; i < words; i++ {
		var input int
		fmt.Scan(&input)
		data[i] = input
	}

	for i := repeats % words; i < words + repeats; i++ {
		fmt.Print(data[i % words])
		fmt.Print(" ")
	}

	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("\nTime Elapsed:", elapsed)
}
