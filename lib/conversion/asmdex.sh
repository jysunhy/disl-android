#!/bin/bash
export LIB_HOME=/home/sunh/disl-android/lib

java -Xmx2g -jar $LIB_HOME/instr.jar $1 $2
