#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
java -Ddislserver.port=6667 -Ddislreserver.ip=127.0.0.1 -Ddislreserver.port=6668 -Ddebug=false -Xmx1g -cp output/lib/analysis.jar:output/lib/dislre-android-server.jar:lib/asm-debug-all-4.1.jar:lib-android/framework.jar ch.usi.dag.disldroidreserver.DiSLREServer 
