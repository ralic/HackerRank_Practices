//https://www.hackerrank.com/challenges/saying-hi?utm_campaign=challenge-recommendation&utm_medium=email&utm_source=24-hour-campaign
package main

import (
	"fmt"
	"log"
	"time"
	"bufio"
	"io"
	"os"
	"regexp"
)
//[Solved]
func main() {
	// Performance Timer
	start := time.Now()

	// Other Code
	var numberLines int
	fmt.Scan(&numberLines)

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

	//fmt.Println(numberLines)

	for i := 0; i < numberLines; i++ {
		//regex, _ := regexp.Compile("^[H|h][I|i]\\s[^D|^d]")
		bool, _ := regexp.MatchString("^[Hh][Ii][ ][^Dd]", lines[i])
		//bool2, _ := regexp.MatchString("[hH][iI][ ][^dD].*", lines[i])
		if (bool) {
			fmt.Print(lines[i])
		}
	}

	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("\nTime Elapsed:", elapsed)
}

// Strange errors : Runtime Error @ Test Case  5/6/9

/* input
7
bottom
dominate weekly cancer challenge prisoner pollution clear
grandmother
closely pronounce make sick statement folding collection yes place
lawyer fire language
Hi penny blankly breath accuse successfully division
Hi gamble give move previously annoyed
*/
/* Output
Hi penny blankly breath accuse successfully division
Hi gamble give move previously annoyed
*/
