package main

import (
	"fmt"
	"time"
	//"log"
	"log"
)

//https://github.com/a8m/go-lang-cheat-sheet

func main() {
	start := time.Now()
	fmt.Println("Hello, 世界")
	elapsed := time.Since(start)
	log.Printf("Time Elapsed:", elapsed)
	fmt.Println("Time Elapsed:", elapsed)
	//end := time.Now();
	//fmt.Println("Time Elapsed:", end.Sub(start))
}
