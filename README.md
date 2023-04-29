# LAB 6: ARXML file organizer

## Description

This script is used to organize the ARXML file in a way that has the containers organized by the name of the container

## How to use

1. run the script providing the path to the ARXML file as an argument as follows:

```
lab6.bat <path_to_arxml_file>
```

## Output

The script will create a folder named "out" in the same directory as the ARXML file and will create a file named
"[input file name]\_mod.arxml" inside it.

## Possible errors

1. no file provided as an argument
   Error: ARXML file path is required as an argument.

2. file does not exist
   Error: ARXML file does not exist.

3. file is not a valid ARXML file or empty
   Error: ARXML file is not valid.
