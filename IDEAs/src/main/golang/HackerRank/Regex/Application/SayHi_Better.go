//https://www.hackerrank.com/challenges/saying-hi?utm_campaign=challenge-recommendation&utm_medium=email&utm_source=24-hour-campaign
package main

import (
	"fmt"
	"time"
	"regexp"
	"bufio"
	"os"
)
//[Solved]
func main() {
	// Performance Timer
	start := time.Now()

	// Other Code
	var numberLines int
	fmt.Scan(&numberLines)
	scanner := bufio.NewScanner(os.Stdin)
	var text string
	for i := 0; i < numberLines; i++ {
		scanner.Scan()
		text = scanner.Text()
		bool, _ := regexp.MatchString("^[Hh][Ii][ ][^Dd].*", text)
		if (bool) {
			fmt.Println(text)
		}
	}

	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("\nTime Elapsed:", elapsed)

}

// All Passed

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
