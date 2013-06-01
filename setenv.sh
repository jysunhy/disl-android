#/bin/bash

Profile="/etc/profile"
#Profile="tmp"

if [[ $ANDROID_PATH != "" ]]
then
	echo "PATH already set"
else
	echo "setting the PATH"
	echo 'export ANDROID_PATH="'` pwd `'"' >> $Profile
	echo 'export PATH="$PATH:$ANDROID_PATH/tools"' >> $Profile
	echo 'export PATH="$PATH:$ANDROID_PATH/platform-tools"' >> $Profile
	echo 'export PATH="$PATH:$ANDROID_PATH/build-tools/android-4.2.2"' >> $Profile
	source $Profile
fi
