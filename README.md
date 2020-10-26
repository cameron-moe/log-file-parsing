# Log File Parsing

A program that takes a log file of API requests and produces reports based off it

The program can create the following reports:

1. Top 10 requested pages and the number of times each page was requested
2. Percentage of successful requests
3. Percentage of unsuccessful requests
4. Top 10 unsuccessful page requests and the number of times each page was requested unsuccessfully
5. Top 10 hosts making the most requests and the number of times each host made a request

## Getting Started

These instructions will show you how to download and run the program

### Installing

A step by step guide to installing and preparing for execution

Step 1:

Clone the log-file-parsing repository to your local machine

`git clone https://github.com/cameron-moe/log-file-parsing.git`

Step 2:

Download the [NASA_access_log_Aug95 file](https://urldefense.com/v3/__ftp://ita.ee.lbl.gov/traces/NASA_access_log_Aug95.gz__;!!GqivPVa7Brio!Kv_gR_pGjGVzr4ZPJtCjYJ1tBUqZXBrt-vbJ2Q1zYWl5FC_g_kyta5MCXsBRddoc5w$) and move the file into the cloned repo with Parser.Java, Testing.Java, blank_log, small_log files.  (This file is 160 MB which is too large to upload to github)

Step 3:

Compile Parser.java: `javac Parser.java`

Step 4:

Compile Testing.java: `javac Testing.java`

## User Guide

### Generating Reports

The main program, Parser.java can be ran in a few ways:

`java Parser.java` - default option, uses NASA_access_log_Aug95 as the default file, and presents user with menu to select reporting option

<img width="600" src="/report_menu.png">


`java Parser.java -f small_log` - using "-f" flag, the file name can be specified in the terminal, for example, this will run using small_log as the file, and then present the user with menu to select reporting option

`java Parser.java -o 3` - using "-o" flag, the reporting option can be specified in the terminal, for example this will run a report on the default file to calculate the percentage of unsuccessful requests (option 3)

`java Parser.java -f small_log -o 2` both the file and the option can be specified in the terminal

Note, whenever the "-o" flag is not given in the terminal, the following menu will be shown until a valid option is chosen

<img width="600" src="/report_menu.png">


### Testing

Unit Testing can be done by running `java -ea Testing`

This will run Testing.java which has a set of assertions to run functions of Parser.java against

## Assumptions/Dependencies

Logs must be in [common log format](https://httpd.apache.org/docs/1.3/logs.html#common)

Logs that are not in this format will be skipped by the program for reporting purposes




