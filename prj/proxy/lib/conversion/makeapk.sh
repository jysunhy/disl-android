#!/bin/bash

function ergodic(){
		rm -rf $1"/tmp"
		mkdir -p $1"/tmp"
        for file in ` ls $1 `
        do
                if [ -d $1"/"$file ]
                then
                        #ergodic $1"/"$file
						echo "it is a directory"
                else
						echo $file
						sudo /home/sunh/conversion/apk2apk.sh $1"/"$file
	#					mkdir $1"/"$file"-files"
						unzip  $1"/"$file -d $1"/"$file"-files/"
						cp /tmp/apk2apk/REPACK/MyApp.apk $1"/tmp/"$file
                fi
        done
}
INIT_PATH="/home/sunh/apks"
ergodic $INIT_PATH
