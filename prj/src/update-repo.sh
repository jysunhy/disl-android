ANDROID_SRC_HOME=~/android-src
cp $ANDROID_SRC_HOME/frameworks/native/libs/binder/*.cpp frameworks/native/libs/binder/
cp $ANDROID_SRC_HOME/frameworks/native/include/binder/*.h frameworks/native/include/binder/
cp $ANDROID_SRC_HOME/frameworks/base/cmds/servicemanager/binder* frameworks/base/cmds/servicemanager/
cp $ANDROID_SRC_HOME/bionic/libc/kernel/common/linux/binder.h  bionic/libc/kernel/common/linux/binder.h
