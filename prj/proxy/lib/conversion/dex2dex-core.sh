#!/bin/bash

# Download from http://code.google.com/p/dex2jar/
export D2J_HOME=/home/zhengy/Tools/dex2jar-0.0.9.15
# Download from http://developer.android.com/sdk/index.html
export ANDROID_HOME=/home/zhengy/Tools/adt-bundle-linux-x86_64-20130522/sdk
# Download from http://code.google.com/p/android-apktool/wiki/ApktoolOptions
export APKTOOL_HOME=/home/zhengy/Tools/apktool1.5.2

export DISL_HOME=/home/zhengy/Tools/disl-offline

export DISL_CLASS=/home/sunh/conversion/DiSLClass.class

echo "Generating .jar"
$D2J_HOME/d2j-dex2jar.sh -f -o $1"_classes.jar" $1

echo "Instrument with DiSL..."
java -Ddisl.classes=$DISL_CLASS -Xmx2g -cp $DISL_HOME/cobertura.jar:$DISL_HOME/asm-debug-all-4.0.jar:$DISL_HOME/dislserver-unspec.jar:$DISL_HOME/log4j-1.2.9.jar net.sourceforge.cobertura.instrument.Main $1"_classes.jar"
jar uf $1"_classes.jar" com/

echo "Generating .dex from .jar"
#$D2J_HOME/d2j-jar2dex.sh -f -o $2 $1"_classes.jar"
/home/sunh/tdroid/out/host/linux-x86/bin/dx --dex --core-library --no-strict --output=$2 $1"_classes.jar"
rm $1"_classes.jar"
