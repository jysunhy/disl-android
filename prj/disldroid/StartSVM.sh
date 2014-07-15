#!/bin/bash
java -Ddislserver.port=6667 -Ddislreserver.ip=127.0.0.1 -Ddislreserver.port=6668 -Ddebug=true -Xmx1g -cp output/lib/dislre-android-server.jar:lib/asm-debug-all-4.1.jar ch.usi.dag.disldroidreserver.DiSLREServer 
