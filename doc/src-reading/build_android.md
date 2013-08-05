##Build Android on Ubuntu10.04
---
	
	#curl https://dl-ssl.google.com/dl/googlesource/git-repo/repo > ./repo
	wget https://dl-ssl.google.com/dl/googlesource/git-repo/repo

	
	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java6-installer
	
	sudo apt-get install git-core gnupg flex bison gperf build-essential \
  	zip curl zlib1g-dev libc6-dev lib32ncurses5-dev ia32-libs \
  	x11proto-core-dev libx11-dev lib32readline5-dev lib32z-dev \
  	libgl1-mesa-dev g++-multilib mingw32 tofrodos python-markdown \
  	libxml2-utils xsltproc
  
  	
  	mkdir android-source
	cd android-source
	
	#repo init -u https://android.googlesource.com/platform/manifest -b android-4.1.1_r6
	repo init -u https://android.googlesource.com/platform/manifest
	repo sync
	make -j8
	make -j8 PRODUCT-sdk-sdk showcommands dist
	
