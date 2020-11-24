#!/bin/bash

case "$1" in
    remote.debug) REMOTE_DEBUG='-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n'; shift ;;
    *)            ;;
esac
case "$1" in
    gc.logging) GC_LOGGING="-XX:+PrintGC -Xloggc:/opt/admin/gcLog-$(date +%s).txt"; shift ;;
    *)          ;;
esac
                 
# all these -d parameters are independent from the server instance. Server-related -d parameters are added by '$*' from the script '_start.sh'
# to enable gc logging add the parameter -XX:+PrintGC -Xloggc:/opt/admin/gcLog-$(date +%s).txt
java $REMOTE_DEBUG $GC_LOGGING -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
     -d server.staticresources.dir=./staticResources \
     -d plugin.tempdir=/tmp/openrobertaTmp \
     -d database.mode=server \
     -d robot.crosscompiler.resourcebase=/opt/ora-cc-rsc \
     -d server.admin.dir=/opt/admin \
     $*
