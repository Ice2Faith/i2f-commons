#!/bin/bash

APP_NAME=common-svc
APP_VERSION=1.0
APP_ENV=product
LOG_MAX_SIZE=20MB
XMX_SIZE=2048M

#JAVA_HOME
export JAVA_HOME=/usr/local/java/jdk1.8.0_144
#export PATH=$JAVA_HOME/bin:$PATH
#export CLASSPATH=.:$JAVA_HOME/jre/lib

# change directory to program dir
FWDIR="$(cd `dirname $0`/..; pwd)"
cd ${FWDIR}

if [ ! -d ${FWDIR}/java.pid ]; then
    touch ${FWDIR}/java.pid
fi

OSUSER=$(id -nu)
PSNUM=$(cat ${FWDIR}/java.pid)
if [[ "$PSNUM" -ne "" ]]; then
    echo ${APP_NAME}" has been started! stop first."
    exit;
fi

${JAVA_HOME}/bin/java -Xmx${XMX_SIZE} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${FWDIR}/ -Dapp.name=${APP_NAME} -Dapp.env=${APP_ENV} -Dmax.size=${LOG_MAX_SIZE} -Duser.dir=${FWDIR} -Dlogging.config=logback-spring.xml -jar ${APP_NAME}-${APP_VERSION}.jar &echo $! > ${FWDIR}/java.pid
exit
