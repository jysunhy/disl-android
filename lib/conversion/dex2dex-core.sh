#!/bin/bash

export LIB_HOME=/home/usi/disl-android/lib

export D2J_HOME=$LIB_HOME/dex2jar-0.0.9.15

export DISL_HOME=$LIB_HOME/disl-offline

export DISL_CLASS=$LIB_HOME/conversion/DiSLClass.class

export DX=/home/usi/tdroid/tdroid-4.1.1_r6/out/host/linux-x86/bin/dx

echo "Generating .jar"
$D2J_HOME/d2j-dex2jar.sh -f -o $1"_classes.jar" $1

echo "Instrument with DiSL..."
java -Ddisl.classes=$DISL_CLASS -Xmx2g -cp $DISL_HOME/cobertura.jar:$DISL_HOME/asm-debug-all-4.0.jar:$DISL_HOME/dislserver-unspec.jar:$DISL_HOME/log4j-1.2.9.jar net.sourceforge.cobertura.instrument.Main $1"_classes.jar"
jar uf $1"_classes.jar" com/

echo "Generating .dex from .jar"
$DX --dex --core-library --no-strict --output=$2 $1"_classes.jar"
#rm $1"_classes.jar"
