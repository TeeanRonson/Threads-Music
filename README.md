SongFinder Labs and Project
===========================


- [Lab 3 - Building a Music Library Data Structure](specifications/lab3.md)

Lab 3 is the first component of the large project you will implement in the second half of the semester. You will use this same repository for the remainder of the semester. As projects are assigned, links to their specifications will be posted here:

- Project Release 1 - Thread-Safe Music Library
- Project Release 2 - Song Search and Web-Basic Interface
- Project Release 3 - Advanced Features

This repository contains the following:

- `src` - the directory that will store your code as well as all of test cases you will be required to pass. `src/test/java` already contains code for the test cases you will need to pass for releases 1 and 2 of the project. Do not be alarmed! For Lab 3 you only need to be concerned with `Lab3Test.java` and `LibraryTest.java`. Do not change any of the test files!
- `expected` - this directory contains the expected output of your solutions. The `songsBy...` text files are the files that will be used for Lab 3.
- `input` - this directory contains the files that will be provided as input to your program when the test cases are executed. Do not change any of these files!
- `queries` - this directory contains additional files that will be used by your program for the second release of the project.
- `specifications` - the descriptions of the assignments
- `pom.xml` - this is the maven configuration file that has already been configured to allow you to use the [GSON](https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.1) API as well as jetty and servlets, which you will use for the project.
- `.travis.yml` - this is the file that will tell Travis which test cases to run. It is currently configured to run only the Lab 3 test cases. 

