#!/bin/bash
java -Ddislserver.port=6667 -Ddislreserver.port=6668 -Ddebug=true -Xmx7g -cp lib/disldroid.jar:lib/asm-debug-all-4.1.jar ch.usi.dag.dislreserver.DiSLREServer 
