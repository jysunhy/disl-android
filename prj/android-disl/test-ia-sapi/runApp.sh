# /bin/sh

# default DiSL lib path
if [ -z "${DISL_LIB_P}" ]; then
    DISL_LIB_P=./build
fi

# test number of arguments
EXPECTED_ARGS=2
if [ $# -lt $EXPECTED_ARGS ]
then
	echo "Usage: `basename $0` instr-lib java-params"
	exit
fi

SERVER_FILE=.server.pid
export SERVER_FILE

RE_SERVER_FILE=.re_server.pid
export RE_SERVER_FILE

# kill running server
if [ -e ${SERVER_FILE} ]
then
    kill -KILL `cat ${SERVER_FILE}`
    rm ${SERVER_FILE}
fi

# kill running server
if [ -e ${RE_SERVER_FILE} ]
then
    kill -KILL `cat ${RE_SERVER_FILE}`
    rm ${RE_SERVER_FILE}
fi

export DISL_LIB_P

# start server (with instrumentation library) and take pid
./runServer.sh $1

# start reserver and take pid
./runREServer.sh $1

# wait for server startup
sleep 3

# run client
./runClient.sh $*

# wait for re server shutdown
RE_SERVER_PID=`cat ${RE_SERVER_FILE}`
while kill -0 "${RE_SERVER_PID}" 2> /dev/null; do
	sleep 0.5
done

# remove server pid files
rm ${SERVER_FILE}
rm ${RE_SERVER_FILE}

