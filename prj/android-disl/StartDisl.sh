#!/bin/bash

java -cp test-ia-sapi/build/disl-instr.jar:build/disl-server.jar ch.usi.dag.dislserver.DiSLServer -Ddislserver.port=6666 -Ddislserver.continuous=true -Xmx6g -Ddebug=false -Ddislrver.instrumented="/tmp/debug/" -Ddisl.exclusionList="excl.txt"
