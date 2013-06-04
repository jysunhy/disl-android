#/bin/bash

Profile="/etc/profile"
#Profile="tmp"

	if [ "$#" -eq 0 ]
	then 
		echo "SDK PATH should be passed"
	else
	echo "setting the PATH"'"'$1'"'
	sudo echo 'export ANDROID_HOME="'$1'"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_HOME/tools"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_HOME/platform-tools"' >> $Profile
	sudo echo 'export PATH="$PATH:$ANDROID_HOME/build-tools/android-4.2.2"' >> $Profile
	fi
