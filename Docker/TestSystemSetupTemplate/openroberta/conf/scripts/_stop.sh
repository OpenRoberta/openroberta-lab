#!/bin/bash

isServerNameValid $SERVER_NAME
SERVERDIR=$SERVER/$SERVER_NAME
isDirectoryValid $SERVERDIR

echo "stopping the server container '$SERVER_NAME'"
DOCKERSTOP=$(docker stop $SERVER_NAME 2>/dev/null)
case "$DOCKERSTOP" in
    '') echo "found no container '$SERVER_NAME'. Nothing stopped" ;;
    * ) echo "stopped container '$DOCKERSTOP'" ;;
esac
DOCKERRM=$(docker rm $SERVER_NAME 2>/dev/null)
case "$DOCKERRM" in
    '') echo "found no container '$SERVER_NAME'. Nothing removed" ;;
    * ) echo "removed container '$DOCKERRM'" ;;
esac