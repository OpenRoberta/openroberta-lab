#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}

cd ${SERVER_DIR_OF_ONE_SERVER}
source ./decl.sh
isDeclShValid

case "${SERVER_NAME}" in
    master) question 'do you really want to deploy master (that would be a PROD deployment)?' ;;
     *)     : ;;
esac
case "${TAG_VERSION}" in
    "") IMAGE_VERSION="${BASE_VERSION}" ;;
    *)  IMAGE_VERSION="${TAG_VERSION}"
        question "do you really want to deploy version ${IMAGE_VERSION} of the server ${SERVER_NAME} (this is VERY unusual)?" ;;
esac
echo "starting server ${SERVER_NAME} based on ora-cc-rsc-version ${BASE_VERSION} with tag ${IMAGE_VERSION}"

IMAGE="openroberta/server_${SERVER_NAME}_${ARCH}:${IMAGE_VERSION}"
CONTAINER="server-${SERVER_NAME}"
RUN="\
docker run -d --name=${CONTAINER} \
--network ${DOCKER_NETWORK_NAME} -p ${PORT}:1999 \
-v ${SERVER_DIR_OF_ONE_SERVER}/admin:/opt/admin \
${IMAGE} \
-d database.uri=db-server \
-d database.name=openroberta-db-${SERVER_NAME} \
-d server.log.configfile=${LOG_CONFIG_FILE} \
-d server.log.level=${LOG_LEVEL} \
${START_ARGS}\
"
echo "starting the server container ${CONTAINER} with ${RUN}"
DOCKERID=$(${RUN})
echo "server container started with id '${DOCKERID}'"
