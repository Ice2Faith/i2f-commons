#!/bin/bash

# jar name
AppName=$1
# control option
ctrlOption=$2
# max wait timeout
MAX_WAIT=30

#JVM参数
JVM_OPTS="-Dname=$AppName  -Duser.timezone=Asia/Shanghai -Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
APP_HOME=`pwd`
LOG_DIR=${APP_HOME}/logs
LOG_PATH=${LOG_DIR}/${AppName}.log


if [ "$ctrlOption" = "" ];
then
    echo -e "\033[0;31m please input 2nd arg:option \033[0m  \033[0;34m {start|stop|restart|status|log|snapshot|backup|recovery|clean} \033[0m"
    echo "start: to run a jar which called AppName"
    echo "stop: to stop a jar which called AppName"
    echo "restart: to stop and run a jar which called AppName"
    echo "status: to check run status for a jar which called AppName"
    echo "log: to lookup the log for a jar which called AppName"
    echo "snapshot: to make a snapshot to ./snapshot for a jar which called AppName"
    echo "backup: to backup to ./backup a jar which called AppName"
    echo "recovery: to recovery from ./backup and save current to ./newest for a jar which called AppName"
    echo "clean: to clean dirs ./backup ./snapshot ./newest ./logs for a jar which called AppName"
    exit 1
fi

if [ "$AppName" = "" ];
then
    echo -e "\033[0;31m please input 1st arg:appName \033[0m"
    exit 1
fi

function start()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`

	if [ x"$PID" != x"" ]; then
	    echo "$AppName is running..."
	else
		chmod a+x $AppName
		mkdir ${LOG_DIR}
		nohup java -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 &
		chmod a+r $LOG_DIR/*.log
		echo "Start $AppName success..."
	fi
}

function stop()
{
    echo "Stop $AppName"

	PID=""
	query(){
	  PID=""
		PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
	}

	query
	if [ x"$PID" != x"" ]; then
		kill -TERM $PID
		echo "$AppName (pid:$PID) exiting..."
		CUR_WAIT=0
		while [ x"$PID" != x"" ]
		do
			sleep 1
			query
			CUR_WAIT=`expr ${CUR_WAIT} + 1`
			echo "wait $AppName stop timeout $CUR_WAIT..."
			if [ $CUR_WAIT == $MAX_WAIT ];then
			  echo "$AppName has timeout of max wait stop,force kill run..."
			  kill -9 $PID
			fi
		done
		echo "$AppName exited."
	else
		echo "$AppName already stopped."
	fi
}

function restart()
{
    stop
    sleep 2
    start
}

function status()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|wc -l`
    if [ $PID != 0 ];then
        echo "$AppName is running..."
    else
        echo "$AppName is not running..."
    fi
}

function log()
{
	tail -f $LOG_PATH
}

function snapshot()
{
  mkdir ./snapshot
  TIME_NOW=$(date "+%Y%m%d%H%M%S")
  cp $AppName ./snapshot/$AppName.$TIME_NOW
  echo "$AppName has snapshot as $AppName.$TIME_NOW."
}

function backup()
{
  mkdir ./backup
  cp $AppName ./backup
  TIME_NOW=$(date "+%Y%m%d%H%M%S")
  echo $TIME_NOW > ./backup/_time
  echo "$AppName has backup."
}

function recovery()
{
  mkdir ./newest
  mv $AppName ./newest
  TIME_NOW=$(date "+%Y%m%d%H%M%S")
  echo $TIME_NOW > ./newest/_time
  cp ./backup/$AppName ./
  echo "$AppName has recovery."
}

function clean()
{
  rm -rf ./backup ./newest ./snapshot ./logs
  echo "clean done."
}

case $ctrlOption in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
	  log)
    log;;
    snapshot)
    snapshot;;
    backup)
    backup;;
    recovery)
    recovery;;
    clean)
    clean;;
    *)

esac
