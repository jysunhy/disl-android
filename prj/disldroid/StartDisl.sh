#!/bin/bash

java -Ddisl.noexcepthandler=true -Ddislserver.port=6667 -Dconfigfile="config-empty.xml" -Ddislserver.continuous=true -Xmx7g -Ddebug=false -Ddislserver.instrumented="/tmp/debug/" -Ddisl.exclusionList="excl.txt" -Ddisl.classes="output/build/analysis/ch/usi/dag/empty/disl/DiSLClass.class" -cp output/lib/disl-server.jar:output/lib/analysis.jar:lib/hamcrest-core-1.3.jar:lib/junit-4.11.jar:lib/ant-contrib-0.6.jar:lib-android/jasmin-p2.5.jar:lib-android/asm-all-3.3.1-jarjar.jar:lib-android/commons-lite-1.15.jar:lib-android/dex-reader-1.15.jar:lib-android/dex-tools-0.0.9.15.jarjar.jar:lib-android/dex-translator-0.0.9.15.jarjar.jar:lib-android/dx.jar:lib-android/asm-debug-all-4.1.jar:lib-android/dex-ir-1.12.jarjar.jar:lib-android/framework.jar ch.usi.dag.disldroidserver.DiSLServer 
