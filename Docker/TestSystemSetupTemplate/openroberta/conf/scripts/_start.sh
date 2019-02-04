#!/bin/bash

isServerNameValid $SERVER_NAME
SERVERDIR=$SERVER/$SERVER_NAME
isDirectoryValid $SERVERDIR

cd $SERVERDIR
source ./decl.sh
isDefined PORT

IMAGE=rbudde/openroberta_lab_$SERVER_NAME:1
D="-d database.uri=$HOSTNAME -d database.name=openroberta-db-$SERVER_NAME $START_ARGS"
echo "starting docker image '$IMAGE' with $D"
DOCKERID=$(docker run -d --name=$SERVER_NAME -p $PORT:1999 $IMAGE $D)
echo "docker container '$DOCKERID' started"
