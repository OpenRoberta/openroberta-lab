#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}

cd ${SERVER_DIR_OF_ONE_SERVER}
source ./decl.sh
isDeclShValid

echo "trying to start server ${SERVER_NAME}"
case "${SERVER_NAME}" in
    master) question 'do you really want to deploy master (that would be a PROD deployment)?'
            case "${OPTIONAL_VERSION}" in
                "") IMAGE_VERSION="${BASE_VERSION}" ;;
                *)  IMAGE_VERSION="${OPTIONAL_VERSION}"
                    question "do you really want to deploy version ${IMAGE_VERSION} of the master image (this is unusual)?" ;;
            esac ;;
     *)     IMAGE_VERSION="${BASE_VERSION}" ;;
esac

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
