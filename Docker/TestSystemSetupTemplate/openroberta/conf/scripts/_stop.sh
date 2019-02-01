#!/bin/bash

isServerNameValid $SERVER_NAME
SERVERDIR=$SERVER/$SERVER_NAME
isDirectoryValid $SERVERDIR

DOCKERSTOP=$(docker stop $SERVER_NAME 2>/dev/null)
case "$DOCKERSTOP" in
    '') echo "found no docker container '$SERVER_NAME'. Nothing stopped" ;;
    * ) echo "stopped docker container '$DOCKERSTOP'" ;;
esac
DOCKERRM=$(docker rm $SERVER_NAME 2>/dev/null)
case "$DOCKERRM" in
    '') echo "found no docker container '$SERVER_NAME'. Nothing removed" ;;
    * ) echo "removed docker container '$DOCKERRM'" ;;
esac