#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}

cd ${SERVER_DIR_OF_ONE_SERVER}
source ./decl.sh
isDeclShValid

case "${SERVER_NAME}" in
    master) question 'do you really want to stop the running container with the MASTER server?' ;;
     *)     : ;;
esac

CONTAINER="server-${SERVER_NAME}"

echo "stopping the server container '${CONTAINER}', if it is running"
DOCKERSTOP=$(docker stop ${CONTAINER} 2>/dev/null)
case "${DOCKERSTOP}" in
    '') echo "found no container '${CONTAINER}' to stop. That is ok" ;;
    * ) echo "stopped container '${DOCKERSTOP}'" ;;
esac
DOCKERRM=$(docker rm ${CONTAINER} 2>/dev/null)
case "${DOCKERRM}" in
    '') echo "found no container '${CONTAINER}' to remove. That is ok" ;;
    * ) echo "removed container '${DOCKERRM}'" ;;
esac
            