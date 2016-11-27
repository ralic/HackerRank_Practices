#!/usr/bin/env bash

#https://leetcode.com/problems/word-frequency/
# Read from the file words.txt and output the word frequency list to stdout.
awk '{for(w=1;w<=NF;w++) print $w}' words.txt | sort | uniq -c | sort -nr | awk '{print $2" "$1}'