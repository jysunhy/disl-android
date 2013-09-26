#export LIB_HOME=/home/sunh/disl-android/lib

java -Xmx2g -jar $LIB_HOME/baksmali-2.0b5.jar -o classout_$1/ $1
java -Xmx2g -jar $LIB_HOME/smali-2.0b5.jar classout_$1/ -o $2
