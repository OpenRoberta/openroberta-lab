#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid $SERVER_DIR_OF_ONE_SERVER

cd $SERVER_DIR_OF_ONE_SERVER
source ./decl.sh
isDeclShValid

case "${SERVER_NAME}" in
    master) question 'do you really want to start a docker image with the MASTER server?'
            question 'you know, that this would be a PROD deployment?' ;;
     *)     : ;;
esac

IMAGE=rbudde/openroberta_${INAME}_${SERVER_NAME}:2
CONTAINER="${INAME}-${SERVER_NAME}"
RUN="\
docker run -d --name=${CONTAINER} \
--network $DOCKER_NETWORK_NAME -p $PORT:1999 \
-v $SERVER_DIR_OF_ONE_SERVER/admin:/opt/admin \
$IMAGE \
-d database.uri=${INAME}-db-server \
-d database.name=openroberta-db-${SERVER_NAME} \
-d server.log.configfile=$LOG_CONFIG_FILE \
-d server.log.level=$LOG_LEVEL \
$START_ARGS\
"
echo "starting the server container ${CONTAINER} with $RUN"
DOCKERID=$($RUN)
echo "server container started with id '$DOCKERID'"
