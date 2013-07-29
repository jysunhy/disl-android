##Build Android
---
	
	curl https://dl-ssl.google.com/dl/googlesource/git-repo/repo > ./repo

	mkdir -p ~/tdroid/tdroid-4.1.1_r6
	cd ~/tdroid/tdroid-4.1.1_r6
	repo init -u https://android.googlesource.com/platform/manifest -b android-4.1.1_r6
	repo sync
	
	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java7-installer
	
	sudo apt-get install git gnupg flex bison gperf build-essential
	sudo apt-get install zip curl libc6-dev libncurses5-dev:i386 x11proto-core-dev
	sudo apt-get install libx11-dev:i386 libreadline6-dev:i386 libgl1-mesa-dri:i386
	sudo apt-get install libgl1-mesa-dev g++-multilib mingw32 tofrodos python-markdown libxml2-utils xsltproc zlib1g-dev:i386
	sudo ln -s /usr/lib/i386-linux-gnu/mesa/libGL.so.1 /usr/lib/i386-linux-gnu/libGL.so
