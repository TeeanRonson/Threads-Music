Lab 3 - Music Library
=====================

For this project, you will implement a program to build a music library data structure to store the information in the last.fm data set available from the [Million Song Dataset](http://labrosa.ee.columbia.edu/millionsong/lastfm).

In this project, you will practice reading and writing files, parsing JSON data, and designing complex data structures. The most important element of this project is to design an efficient data structure.
 
## Functionality

**Driver** - You must have a class `songfinder.Driver` that contains your main method. The skeleton of this class has been provided for you.

**Main Logic** - The main logic of your program will proceed as follows:

1. Given a directory, find all .json files in the directory or any subdirectory, where each .json file contains information about a single song. 

2. As you read each file, add the song information (artist, title, track_id, and similars) to a data structure. 
	
3. Once you have built your entire library, you will save the text into a single .txt file, sorted in a given order.

**Execution** - Your program will expect three arguments to be passed as command line input, specified using flags. An example valid execution of your program would look as follows: `java songfinder.Driver -input input/lastfm_subset -output /Users/srollins/cs212/Project/results -order tag`. If any of the three required flags is missing, or if any flag is not followed by an appropriate value, your program must exit gracefully (not by throwing exceptions!). The flags and their values may be reordered, so the following is also valid: `java songfinder.Driver -output /Users/srollins/cs212/Project/results -order tag -input /Users/srollins/cs212/Project/input/lastfm_subset ` Example **invalid** executions would include `java songfinder.Driver hello world` and `java songfinder.Driver -input input/lastfm_subset -order title`. 

**Arguments - input** - The value following the `-input` flag will be the top-level directory containing the song data, in .json format. If the directory specified is not valid your program must exit gracefully. The value may specify a directory using *either* a relative or absolute path. 

**Arguments - output** - The value following the `-output` flag will be the name of the specific file where your program will save its output. The value may contain a file name, absolute path, or relative path. If the path specified is not valid, for example it specifies a directory that does not exist (e.g., `/BAD/file.txt`), your program must exit gracefully. 

**Arguments - order** - The value following the `-order` flag specifies the ordering to use when saving the music library data to a file. There are three possible valid values: `artist`, `title`, and `tag`. If the value specified is not one of these values, your program will exit without saving any data. For the three valid values, the expected output will look as follows:

 - Data sorted by `artist` will list the artist name, followed by a space, followed by `-`, followed by a space, followed by `title`, followed by a new line, and will be in alphabetical order by artist. If two songs have the same artist, they will then be sorted by the title. If two songs have the same artist and title, they will be sorted by the track_id. Example:

  ```
  Steel Rain - Loaded Like A Gun
  Tom Petty - A Higher Place (Album Version)
  ```

 - Data sorted by `title` will list the artist name, followed by a space, followed by `-`, followed by a space, followed by `title`, followed by a new line, and will be in alphabetical order by title. If two songs have the same title, they will then be sorted by the artist. If two songs have the same title and artist, they will be sorted by the track_id. Example:

  ``` 
  Hushabye Baby - Cry, Cry, Cry
  Aerosmith - Cryin'
  ```

 - Data sorted by `tag` will list the tag, followed by `:`, followed by a space, followed by the track_ids of all songs with that tag, separated by spaces. Data will be in alphabetical order by tag. 

  ```
  Best of Bon Jovi: TRAUYZG12903CDA4E9 TRAZJOI12903CDA550 TRBAAOT128F4261A18 
  Best of Grunge: TRAVBAH128F9305DFC 
  ```

## Requirements and Hints 

- Use the `UTF-8` character encoding for all file processing (including reading and writing).
- Process all .json files, case **insensitive**, and ignore any files with a different extension.
- You are expected to use object-oriented design. As a hint, my solution uses four classes in addition to the driver: a class that parses the command line arguments; a class that stores data for a single song; a class that stores the entire library and has several complex data structures as its data members; and a class that traverses the file system and builds the library.
- Your main method should do very little.
- You will need to implement your own test code as you go. Do not rely only on the unit tests.
- Thorough error checking is required. Make sure to handle exceptions appropriately and verify all of your inputs.

### Submission Requirements

1. Use the following link to create your private github repository for this assignment. The repository will be seeded with the skeleton code, test cases, and input files. [Lab 3]()
2. For full credit, make sure to follow all [Style Guidelines](https://github.com/CS514-F17/notes/blob/master/Admin/style.md). Points will be deducted for each violation.


### Grading Rubric

| Points | Criterion |
| ------ | -------- |  
| 5 | testBadInputPath |
| 5 | testBadOutputPath |
| 5 | testBadOrderValue |
| 7 | testSimpleByArtist |
| 6 | testSimpleByTitle |
| 6 | testSimpleByTag |
| 6 | testSimpleByArtistWithTxtFile |
| 15 | testByArtist |
| 15 | testByTitle |
| 15 | testByTag |
| 15 | Design |

Partial credit may be awarded for partial functionality and/or partially correct design or style elements.

### Academic Dishonesty

Any work you submit is expected to be your own original work. If you use any web resources in developing your code you are strongly advised to cite those resources. The only exception to this rule is code that is posted on the class website. The URL of the resource you used in a comment in your code is fine. If I google even a single line of uncited code and find it on the internet you may get a 0 on the assignment or an F in the class. You may also get a 0 on the assignment or an F in the class if your solution is at all similar to that of any other student.
