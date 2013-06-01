#/bin/bash

./setenv.sh
	
#android list avd
#emulator -avd 'name'

android update project --path NotePad/ --target 1 --subprojects
cd NotePad/
ant debug
#ant debug install


