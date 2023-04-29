@echo off

@REM delete the build directory and out.jar
rmdir /s /q build
if exist out.jar del out.jar

@REM create the build directory
mkdir build

@REM compile the java files
javac -d build src\*.java

@REM create manifest file if it doesn't exist
if not exist manifest.txt echo Main-Class: Main > manifest.txt

@REM create the jar file
jar cfm out.jar manifest.txt -C build .

@REM run the jar file and pass all arguments
java -jar out.jar %*