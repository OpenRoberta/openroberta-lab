#!/bin/bash

isServerNameValid $SERVER_NAME
SERVERDIR=$SERVER/$SERVER_NAME
isDirectoryValid $SERVERDIR

cd $SERVERDIR
source ./decl.sh
isDefined PORT

IMAGE=rbudde/openroberta_lab_$SERVER_NAME:1
D="-d database.uri=ora-db-server -d database.name=openroberta-db-$SERVER_NAME $START_ARGS"
echo "starting the server image '$IMAGE' with $D"
DOCKERID=$(docker run -d --name=$SERVER_NAME --network ora-net -p $PORT:1999 $IMAGE $D)
echo "server container started with id '$DOCKERID'"
