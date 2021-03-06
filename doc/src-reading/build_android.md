##Build Android on Ubuntu13.04
---
	
	#curl https://dl-ssl.google.com/dl/googlesource/git-repo/repo > ./repo
	wget https://dl-ssl.google.com/dl/googlesource/git-repo/repo

	
	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java6-installer
	
	sudo apt-get install git-core gnupg flex bison gperf build-essential zip curl zlib1g-dev libc6-dev lib32ncurses5-dev ia32-libs x11proto-core-dev libx11-dev lib32readline5-dev lib32z-dev libgl1-mesa-dev g++-multilib mingw32 tofrodos python-markdown libxml2-utils xsltproc
  
  //Ubuntu 14
  sudo apt-get install libswitch-perl  
  
  	mkdir android-source
	cd android-source
	
	repo init -u https://android.googlesource.com/platform/manifest -b android-4.1.1_r6
	#repo init -u https://android.googlesource.com/platform/manifest
	repo sync

##Compile and run
	make -j16 showcommands WITH_DEXPREOPT=false 
	//or you can change the true to false in build/target/board/generic/BoardConfig.mk
	
	#make -j8
	#make -j8 PRODUCT-sdk-sdk showcommands dist
	
	make sure java javac javadoc are of version 1.6
	
	emulator -sysdir out/target/product/generic/ -system out/target/product/generic/system.img -ramdisk out/target/product/generic/ramdisk.img -data out/target/product/generic/userdata.img -kernel prebuilts/qemu-kernel/arm/kernel-qemu -sdcard sdcard.img -skindir sdk/emulator/skins -skin WVGA800 -scale 0.7 -memory 512 -partition-size 1024

	./out/host/linux-x86/bin/emulator -sysdir out/target/product/generic/ -system out/target/product/generic/system.img -ramdisk out/target/product/generic/ramdisk.img -data out/target/product/generic/userdata.img -kernel prebuilts/qemu-kernel/arm/kernel-qemu-armv7 -sdcard sdcard.img -skindir development/tools/emulator/skins -skin HVGA -scale 0.7 -memory 512 -partition-size 1024

###mmm can be used to compile single module [refer.](http://blog.csdn.net/luoshengyang/article/details/6566662)
	mmm packages/apps/Email/	//generate Email.app to app folder in out/..
	make snod			//repackage the system.img

### Compile Kernel

####ARM
(according to external/qemu/docs/ANDROID-KERNEL.TXT)
//git clone git://android.git.kernel.org/kernel/common.git kernel-common
//cd kernel-common
//git checkout origin/archive/android-gldfish-2.6.29

git clone https://android.googlesource.com/kernel/goldfish.git
git checkout -b android-goldfish-2.6.29 origin/android-goldfish-2.6.29

export CROSS_COMPILE=arm-eabi-
export ARCH=arm
export SUBARCH=arm
make goldfish_defconfig
make

### x86
git clone http://android.googlesource.com/kernel/goldfish.git goldfish-kernel
cd goldfish-kernel
git checkout android-goldfish-3.4
make ARCH=x86 goldfish_defconfig
export CROSS\_COMPILE=<AOSP_TREE>/prebuilts/gcc/linux-x86/x86/i686-linux-android-4.7/bin/i686-linux-android-
make ARCH=x86 CC="${CROSS_COMPILE}gcc -mno-android" bzImage

### DEBUG
adb logcat -s TAG:[i|e|...]

### make
make snod and emulator builds.
Symptom: When using make snod (make system no dependencies) on emulator builds, the resulting build doesn't work.

Cause: All emulator builds now run Dex optimization at build time by default, which requires to follow all dependencies to re-optimize the applications each time the framework changes.

Fix: Locally disable Dex optimizations with export WITH_DEXPREOPT=false, delete the existing optimized versions with make installclean and run a full build to re-generate non-optimized versions. After that, make snod will work.


