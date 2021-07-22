#!/bin/bash

for f in `find . -name "*.java"`
do
	echo "Compiling $f..."
	javac "$f"
done

