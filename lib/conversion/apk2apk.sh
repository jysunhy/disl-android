#!/bin/bash

# Download from http://code.google.com/p/dex2jar/
export D2J_HOME=/home/zhengy/Tools/dex2jar-0.0.9.15
# Download from http://developer.android.com/sdk/index.html
export ANDROID_HOME=/home/zhengy/Tools/adt-bundle-linux-x86_64-20130522/sdk
# Download from http://code.google.com/p/android-apktool/wiki/ApktoolOptions
export APKTOOL_HOME=/home/zhengy/Tools/apktool1.5.2

export DISL_HOME=/home/zhengy/Tools/disl-offline

export DEST=/tmp/apk2apk

export DISL_FILE=/home/sunh/conversion/DiSLClass.class

rm -rf $DEST
mkdir -p $DEST/DEX
mkdir -p $DEST/RES
mkdir -p $DEST/CLASSES
mkdir -p $DEST/REPACK


echo "Unpacking with apktool..."
$APKTOOL_HOME/apktool d $1 $DEST/APKTOOL

echo "Generating .jar"
$D2J_HOME/d2j-dex2jar.sh -o $DEST/DEX/classes.jar $1

echo "Instrument with DiSL..."
java -Ddisl.classes=$DISL_FILE -Xmx1g -cp $DISL_HOME/cobertura.jar:$DISL_HOME/asm-debug-all-4.0.jar:$DISL_HOME/dislserver-unspec.jar:$DISL_HOME/log4j-1.2.9.jar net.sourceforge.cobertura.instrument.Main $DEST/DEX/classes.jar
jar uf $DEST/DEX/classes.jar com/

echo "Generating .dex from .jar"
echo "hhhhh"
$D2J_HOME/d2j-jar2dex.sh -o $DEST/CLASSES/classes.dex $DEST/DEX/classes.jar


echo "Copying resources"
cp -r $DEST/APKTOOL/res $DEST/RES

echo "Repacking..."
$ANDROID_HOME/build-tools/android-4.2.2/aapt p -v -f -M $DEST/APKTOOL/AndroidManifest.xml -S $DEST/RES/res -I $ANDROID_HOME/platforms/android-17/android.jar -F $DEST/REPACK/MyApp.unsigned.apk  $DEST/CLASSES

echo "Signing apk.."
# the "-sigalg MD5withRSA -digestalg SHA1" is for JDK 1.7,remove if not needed
jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1  -keystore AndroidTest.keystore -storepass password -keypass password -signedjar $DEST/REPACK/MyApp.signed.apk $DEST/REPACK/MyApp.unsigned.apk AndroidTestKey

$ANDROID_HOME/tools/zipalign -v -f 4 $DEST/REPACK/MyApp.signed.apk $DEST/REPACK/MyApp.apk
echo "...."
echo "DONE: App ready to deploy... Just run:"
echo "\$ANDROID_HOME/platform-tools/adb -s emulator-5554 install /tmp/apk2apk/REPACK/MyApp.apk"






