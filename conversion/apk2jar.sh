#!/bin/bash

echo "argv[1] -> original.apk"
echo "argv[2] -> original.jar"

mkdir -p bin

DEX2JAR_PATH="/home/user/dex2jar-0.0.9.15"

#get the jar
$DEX2JAR_PATH/d2j-dex2jar.sh -f -o bin/original.jar $1

#verify the jar
$DEX2JAR_PATH/d2j-asm-verify.sh bin/original.jar


