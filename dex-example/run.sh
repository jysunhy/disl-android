#!/bin/bash

ant

LIBDEX=../lib/asmdex-1.0.jar
java -classpath bin:$LIBDEX  SimpleTest $1
