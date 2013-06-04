#/bin/bash

Profile="/etc/profile"
#Profile="tmp"

if [[ $ANDROID_PATH != "" ]]
then
	echo "PATH already set"
else
	if [[ $# == 0 ]]
	then echo "SDK PATH should be passed"
	else
	echo "setting the PATH"
	sudo echo 'export ANDROID_PATH="'` pwd `'"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_PATH/tools"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_PATH/platform-tools"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_PATH/build-tools/android-4.2.2"' >> $Profile
	source $Profile
	fi
fi
