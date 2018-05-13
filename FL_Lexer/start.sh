#!/bin/bash
if [ "$1" = "compile" ]
	then 
		mvn compile && mvn test
fi
if [ "$1" = "run" ]
	then
		java -classpath ./target/classes Main
fi