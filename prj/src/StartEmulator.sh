#/bin/bash
source build/envsetup.sh

emulator -sysdir out/target/product/generic/ -system out/target/product/generic/system.img -ramdisk out/target/product/generic/ramdisk.img -data out/target/product/generic/userdata.img -kernel prebuilts/qemu-kernel/arm/kernel-qemu-armv7 -memory 512 -partition-size 1024
