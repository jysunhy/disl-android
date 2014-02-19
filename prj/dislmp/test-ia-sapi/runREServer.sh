#!/bin/sh

# set default lib path
if [ -z "${DISL_LIB_P}" ]; then
	DISL_LIB_P=./build
fi

# available options
#    -Ddebug=true \
#    -Ddislreserver.port="portNum" \

# get instrumentation library and shift parameters
INSTR_LIB=$1
shift

# start server
java $* \
     -Xmx10g \
     -cp ${INSTR_LIB}:${DISL_LIB_P}/dislre-server.jar \
     ch.usi.dag.dislreserver.DiSLREServer \
     &

# print pid to the server file
if [ -n "${RE_SERVER_FILE}" ]; then
    echo $! > ${RE_SERVER_FILE}
fi
