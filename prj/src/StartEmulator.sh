#/bin/bash
source build/envsetup.sh
adb logcat >~/log & 2>&1
emulator -sdcard sdcard.img -sysdir out/target/product/generic/ -system out/target/product/generic/system.img -ramdisk out/target/product/generic/ramdisk.img -data out/target/product/generic/userdata.img -kernel zImage -memory 2048 -partition-size 1024
