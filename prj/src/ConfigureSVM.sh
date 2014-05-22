echo "******************************************"
echo "Configure Properties Before Start Emulator"
echo ""
LOCAL_IP=` LC_ALL=C ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}' `

CONFIGFILE="out/target/product/generic/system/svm.prop"
touch $CONFIGFILE

old1=` cat $CONFIGFILE | awk '{ print $1}' `
if [ "$old1" == "" ]
then
	old1=$LOCAL_IP
else
	old1=$old1
fi
old2=` cat $CONFIGFILE | awk '{ print $2}' `
if [ "$old2" == "" ]
then
	old2=6668
else
	old2=$old2
fi
old3=` cat $CONFIGFILE | awk '{ print $3}' `
if [ "$old3" == "" ]
then
	old3=$LOCAL_IP
else
	old3=$old3
fi
old4=` cat $CONFIGFILE | awk '{ print $4}' `
if [ "$old4" == "" ]
then
	old4=6667
else
	old4=$old4
fi

echo $old1 $old2 $old3 $old4 > $CONFIGFILE

echo "current Analysis   ($old1:$old2)" 
echo "current Instrument ($old3:$old4)"
echo ""


read -p "Analysis Server IP (default-"$old1"):    " svmIP
if [ "$svmIP" == "" ]
then
	svmIP=$old1
else
	svmIP=$svmIP
fi
#echo ""
#echo "-Analysis Server IP to be set to "$svmIP
#echo ""

read -p "Analysis Server PORT (default-$old2):      " svmPort
if [ "$svmPort" == "" ]
then
	svmPort=$old2
else
	svmPort=$svmPort
fi
#echo ""
#echo "-Analysis Server Port to be set to "$svmPort
#echo ""

read -p "Instrument Server IP (default-$old3):  " dislIP
if [ "$dislIP" == "" ]
then
	dislIP=$old3
else
	dislIP=$dislIP
fi
#echo ""
#echo "-Instrument Server IP to be set to "$dislIP
#echo ""

read -p "Instrument Server PORT (default-$old4):     " dislPort
if [ "$dislPort" == "" ]
then
	dislPort=$old4
else
	dislPort=$dislPort
fi
#echo ""
#echo "-Instrument Server Port to be set to "$dislPort
#echo ""


echo $svmIP $svmPort $dislIP $dislPort > out/target/product/generic/system/svm.prop
make snod

echo ""
echo "new Analysis   ($svmIP:$svmPort)" 
echo "new Instrument ($dislIP:$dislPort)"
echo "**********Configure Successfull***********"
