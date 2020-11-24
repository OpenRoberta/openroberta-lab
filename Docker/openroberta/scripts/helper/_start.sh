#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}

cd ${SERVER_DIR_OF_ONE_SERVER}
source ./decl.sh
isDeclShValid

case "${SERVER_NAME}" in
    master) if [ "${REMOTE_DEBUG}" == 'true' ]
            then
                echo 'remote debugging is forbidden on master. exit 12'
                exit 12
            fi
            question 'do you really want to deploy master (that would be a PROD deployment)?' ;;
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
if [ "${REMOTE_DEBUG}" == 'true' ]
then
    REMOTE_DEBUG_PARAM='remote.debug'
    REMOTE_DEBUG_PORT='-p 8000:8000'
else
    REMOTE_DEBUG_PARAM=''
    REMOTE_DEBUG_PORT=''
fi
if [ "${GC_LOGGING}" == 'true' ]
then
    GC_LOGGING_PARAM='gc.logging'
else
    GC_LOGGING_PARAM=''
fi
if $(isWin)
then
    export MSYS_NO_PATHCONV=1
fi
RUN="\
docker run -d --name=${CONTAINER} \
--network ${DOCKER_NETWORK_NAME} -p ${PORT}:1999 $REMOTE_DEBUG_PORT \
-v ${SERVER_DIR_OF_ONE_SERVER}/admin:/opt/admin \
${IMAGE} $REMOTE_DEBUG_PARAM $GC_LOGGING_PARAM \
-d database.uri=db-server \
-d database.name=openroberta-db-${SERVER_NAME} \
-d server.log.configfile=${LOG_CONFIG_FILE} \
-d server.log.level=${LOG_LEVEL} \
${START_ARGS}\
"
echo "starting the server container ${CONTAINER} with ${RUN}"
DOCKERID=$(${RUN})
echo "server container started with id '${DOCKERID}'"
