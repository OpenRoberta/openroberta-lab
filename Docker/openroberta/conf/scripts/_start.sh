#!/bin/bash

isServerNameValid $SERVER_NAME
SERVER_DIR_OF_ONE_SERVER=$SERVER_DIR/$SERVER_NAME
isDirectoryValid $SERVER_DIR_OF_ONE_SERVER

cd $SERVER_DIR_OF_ONE_SERVER
source ./decl.sh
isDefined PORT
isDefined LOG_LEVEL
isDefined LOG_CONFIG_FILE

case "$SERVER_NAME" in
    master) question 'do you really want to stop the running container with the MASTER server and start a docker image with the MASTER server?'
            question 'you know, that this would be a PROD deployment?' ;;
     *)     : ;;
esac

IMAGE=rbudde/openroberta_lab_$SERVER_NAME:2
RUN="\
docker run -d --name=$SERVER_NAME \
--network ora-net -p $PORT:1999 \
-v $SERVER_DIR_OF_ONE_SERVER/admin:/opt/admin \
$IMAGE \
-d database.name=openroberta-db-$SERVER_NAME \
-d server.log.configfile=$LOG_CONFIG_FILE \
-d server.log.level=$LOG_LEVEL \
$START_ARGS\
"
echo "starting the server with $RUN"
DOCKERID=$($RUN)
echo "server container started with id '$DOCKERID'"
