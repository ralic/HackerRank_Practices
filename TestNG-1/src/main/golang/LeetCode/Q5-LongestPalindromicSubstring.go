//https://leetcode.com/problems/longest-palindromic-substring/
package main

import (
	"fmt"
	"time"
	"bufio"
	"os"
	"strings"
)
/* Input
babad"
 */

/* Output
babad"
 */
func main() {
	// Performance Timer
	start := time.Now()

	// Other Code
	scanner := bufio.NewScanner(os.Stdin)

	var text string
	var data []string
	for i := 0; i < 1; i++ {
		scanner.Scan()
		text = scanner.Text()
		data = strings.Split(text, "");
	}
	fmt.Println(data)
	var final []string
	var central int = 0;
	for i := 0; i < len(data) - 1; i++ {
		for j := len(data) - 1; j >= i; j-- {
			if (data[i] == data[j]) {
				if (i == j) {
					central = i
				}
				final = append(final, data[i])
				fmt.Println("--central", central, i, j, data[i], data[j])

			}

			fmt.Println("central", central, i, j)
		}
	}

	fmt.Println(final)



	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("\nTime Elapsed:", elapsed)
}

