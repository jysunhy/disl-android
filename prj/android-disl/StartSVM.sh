#!/bin/bash
java -Ddislserver.port=6667 -Ddislreserver.port=6668 -Ddebug=true -Xmx7g -cp build/dislre-server.jar:bin/ ch.usi.dag.dislreserver.DiSLREServer 
