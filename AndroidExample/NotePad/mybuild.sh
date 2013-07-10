#!/bin/bash
##Generate the R.java

rm -rf bin
rm -rf assets
rm -rf gen

mkdir bin
mkdir assets
mkdir gen

##generate R.java
aapt package -f -m -J gen -S res -I $ANDROID_HOME/platforms/android-17/android.jar -M AndroidManifest.xml
##generate classes
javac -encoding UTF-8 -target 1.5 -bootclasspath $ANDROID_HOME/platforms/android-17/android.jar -d bin src/com/example/android/notepad/*.java gen/com/example/android/notepad/R.java
##dx compile
dx --dex --output=bin/classes.dex bin
##generate apk install package
#apkbuilder bin/Notepad_usigned.apk -v -u -z bin/resources.ap_ -f bin/classes.dex -rf src
#aapt p[ackage] [-d][-f][-m][-u][-v][-x][-z][-M AndroidManifest.xml] \
#        [-0 extension [-0 extension ...]] [-g tolerance] [-j jarfile] \
#        [--debug-mode] [--min-sdk-version VAL] [--target-sdk-version VAL] \
#        [--app-version VAL] [--app-version-name TEXT] [--custom-package VAL] \
#        [--rename-manifest-package PACKAGE] \
#        [--rename-instrumentation-target-package PACKAGE] \
#        [--utf16] [--auto-add-overlay] \
#        [--max-res-version VAL] \
#        [-I base-package [-I base-package ...]] \
#        [-A asset-source-dir]  [-G class-list-file] [-P public-definitions-file] \
#        [-S resource-sources [-S resource-sources ...]] \
#        [-F apk-file] [-J R-file-dir] \
#        [--product product1,product2,...] \
#        [-c CONFIGS] [--preferred-configurations CONFIGS] \
#        [raw-files-dir [raw-files-dir] ...] \
#        [--output-text-symbols DIR]

#   Package the android resources.  It will read assets and resources that are
#   supplied with the -M -A -S or raw-files-dir arguments.  The -J -P -F and -R
#   options control which files are output.

##generate bin/resources.ap_
aapt package -f -M AndroidManifest.xml -S res -A assets -I $ANDROID_HOME/platforms/android-17/android.jar -F bin/resources.ap_

aapt package -f -M AndroidManifest.xml -A assets -S res -I $ANDROID_HOME/platforms/android-17/android.jar 
