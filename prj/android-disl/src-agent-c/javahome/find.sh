#!/bin/sh

# use variable specified by the user
if [ -f "var.local" ]; then
	cp -f "var.local" "var"
	exit 0	
fi

# use environment variable
if [ ! -z "$JAVA_HOME" ]; then
	echo "JAVA_HOME="$JAVA_HOME > "var"
	exit 0
fi

# guess
JAVAC=`which javac 2>/dev/null`
JH=`readlink -f ${JAVAC}`
JH=`echo ${JH%/*}`
JH=`echo ${JH%/*}`
echo "JAVA_HOME="${JH} > "var"

