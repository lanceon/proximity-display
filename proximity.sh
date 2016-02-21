#!/bin/bash

MAIN_JAR="proximity.jar"

DEPS_JAR="proximity-display-assembly-1.0-deps.jar"

MAIN_CLASS="com.devscala.explorer.ProximityApp"

MONITORING="-Djava.rmi.server.hostname=192.168.0.20 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1088 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

LOGGING="-Dlogback.configurationFile=logback.xml"

#FORCE_COMPILE="-XX:CompileThreshold=1"
FORCE_COMPILE=""

echo "Starting ${MAIN_JAR}..."

sudo java ${MONITORING} ${FORCE_COMPILE} ${LOGGING} -cp .:${MAIN_JAR}:${DEPS_JAR} ${MAIN_CLASS} $@

echo "Script has finished"
