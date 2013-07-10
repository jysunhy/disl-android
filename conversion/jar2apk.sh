#!/bin/bash

echo "argv[1] -> instrumented.jar"
echo "argv[2] -> original.apk"
echo "output instrumented.apk to bin/"

DEX2JAR_PATH="/home/user/dex2jar-0.0.9.15"

mkdir -p bin

rm -f bin/tmp.apk
rm -f  bin/classes.dex
rm -f bin/instrumented.apk

#get the jar
$DEX2JAR_PATH/d2j-jar2dex.sh  -f -o bin/classes.dex $1

cp $2 bin/tmp.apk

zip -r bin/tmp.apk bin/classes.dex

$DEX2JAR_PATH/d2j-apk-sign.sh -f -o bin/instrumented.apk bin/tmp.apk

rm bin/tmp.apk
rm bin/classes.dex
