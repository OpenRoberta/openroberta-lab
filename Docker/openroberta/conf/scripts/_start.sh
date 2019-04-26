#!/bin/bash

isServerNameValid $SERVER_NAME
SERVER_DIR_OF_ONE_SERVER=$SERVER_DIR/$SERVER_NAME
isDirectoryValid $SERVER_DIR_OF_ONE_SERVER

cd $SERVER_DIR_OF_ONE_SERVER
source ./decl.sh
isDefined PORT

case "$SERVER_NAME" in
    master) question 'do you really want to stop the running container with the MASTER server and start a docker image with the MASTER server?'
            question 'you know, that this ia a PROD deployment?' ;;
     *)     : ;;
esac

IMAGE=rbudde/openroberta_lab_$SERVER_NAME:2
D="-d database.uri=ora-db-server -d database.name=openroberta-db-$SERVER_NAME $START_ARGS"
echo "starting the server image '$IMAGE' with $D"
DOCKERID=$(docker run -d --name=$SERVER_NAME --network ora-net -p $PORT:1999 $IMAGE $D)
echo "server container started with id '$DOCKERID'"
