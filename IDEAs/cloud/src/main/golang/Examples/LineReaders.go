package main

import (
	"fmt"
	"time"
	"bufio"
	"os"
)

func main() {
	// Performance Timer
	start := time.Now()

	// Other Code
	// Other Code
	var numberLines int
	fmt.Scan(&numberLines)
	// Reader -1
	scanner := bufio.NewScanner(os.Stdin)
	// Reader -2
	reader := bufio.NewReader(os.Stdin)

	var text string
	for i := 0; i < numberLines; i++ {
		scanner.Scan()
		//Reader -1
		text = scanner.Text()
		fmt.Println("reader1:", text)
		//Reader -2
		text, _ = reader.ReadString(byte('\n'))
		fmt.Print("reader2:", text)
		//Reader -3
		text, _ = reader.ReadString('\n')
		fmt.Print("reader3:", text)
	}

	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("\nTime Elapsed:", elapsed)

}
