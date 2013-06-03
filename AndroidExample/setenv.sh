#/bin/bash

Profile="/etc/profile"
#Profile="tmp"

if [[ $ANDROID_PATH != "" ]]
then
	echo "PATH already set"
else
	echo "setting the PATH"
	sudo echo 'export ANDROID_PATH="'` pwd `'"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_PATH/tools"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_PATH/platform-tools"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_PATH/build-tools/android-4.2.2"' >> $Profile
	source $Profile
fi
