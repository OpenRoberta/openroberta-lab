#!/bin/bash

# dangerous parameters! Use not for production!
REMOTE_DEBUG=''
GC_LOGGING=''
while true
do
  case "$1" in
    remote.debug) REMOTE_DEBUG='-agentlib:jdwp=transport=dt_socket,address=0.0.0.0:2000,server=y,suspend=y'; shift ;;
    gc.logging)   GC_LOGGING="-XX:+PrintGC"; shift ;;
    *)            break ;;
  esac
done
                 
# all these -d parameters are independent from the server instance. Server-related -d parameters are added by '$*'
java $REMOTE_DEBUG $GC_LOGGING -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
     -d server.staticresources.dir=./staticResources \
     -d plugin.tempdir=/tmp/openrobertaTmp \
     -d database.mode=server \
     -d robot.crosscompiler.resourcebase=/opt/ora-cc-rsc \
     -d server.admin.dir=/opt/admin \
     $*
