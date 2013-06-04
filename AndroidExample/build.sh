#/bin/bash

if [[ $ANDROID_HOME != "" ]]
then
#android list avd
#emulator -avd 'name'

android update project --path NotePad/ --target 1 --subprojects
cd NotePad/
ant debug
else
        echo "set the env path first"
#ant debug install
fi

