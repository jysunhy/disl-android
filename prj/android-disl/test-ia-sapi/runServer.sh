#!/bin/sh

# set default lib path
if [ -z "${DISL_LIB_P}" ]; then
	DISL_LIB_P=./build
fi

# available options
#    -Ddebug=true \
#    -Ddisl.classes="list of disl classes (: - separator)"
#    -Ddisl.noexcepthandler=true \
#    -Ddisl.exclusionList="path" \
#    -Ddislserver.instrumented="path" \
#    -Ddislserver.uninstrumented="path" \
#    -Ddislserver.port="portNum" \
#    -Ddislserver.timestat=true \
#    -Ddislserver.continuous=true \

# get instrumentation library and shift parameters
INSTR_LIB=$1
shift

# start server
java $* \
     -cp ${INSTR_LIB}:${DISL_LIB_P}/disl-server.jar \
     -Ddisl.noexcepthandler=true \
     -Ddislserver.disablebypass=true \
     ch.usi.dag.dislserver.DiSLServer \
     &

# print pid to the server file
if [ -n "${SERVER_FILE}" ]; then
    echo $! > ${SERVER_FILE}
fi
