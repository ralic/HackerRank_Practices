//https://www.hackerrank.com/challenges/hackerrank-language
package main

import (
	"fmt"
	"time"
	"bufio"
	"os"
	"regexp"
	"strings"
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
		validateLang(text)
	}
	// Performance Report
	elapsed := time.Since(start)
	fmt.Println("\nTime Elapsed:", elapsed)
}

func validateLang(text string) {
	var validString string = "C:CPP:JAVA:PYTHON:PERL:PHP:RUBY:CSHARP:HASKELL:CLOJURE:BASH:SCALA:ERLANG:CLISP:LUA:BRAINFUCK:JAVASCRIPT:GO:D:OCAML:R:PASCAL:SBCL:DART:GROOVY:OBJECTIVEC"
	var setofValidString = strings.Split(validString, ":")

	for j := 0; j < len(setofValidString); j++ {
		bool, _ := regexp.MatchString("^\\d{4,5} " + setofValidString[j] + "$", text)
		if (bool) {
			fmt.Println("VALID")
			return
		}
	}
	fmt.Println("INVALID")
}