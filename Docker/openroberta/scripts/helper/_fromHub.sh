#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}

source ${SERVER_DIR_OF_ONE_SERVER}/decl.sh
isDeclShValid
isDefined BASE_VERSION

IMAGE="openroberta/server_${SERVER_NAME}_${ARCH}:${BASE_VERSION}"
DOCKERRM=$(docker rmi ${IMAGE} 2>/dev/null)
case "${DOCKERRM}" in
    '') echo "found no docker image '${IMAGE}' to remove. That is ok." ;;
    * ) echo "removed old docker image '${IMAGE}'"
esac
DOCKERRM=$(docker rmi ${REPO_NAME} 2>/dev/null)
case "${DOCKERRM}" in
    '') echo "found no docker image '${REPO_NAME}' to remove. That is ok." ;;
    * ) echo "removed old docker image '${REPO_NAME}'"
esac
docker pull ${REPO_NAME}
docker tag ${REPO_NAME} ${IMAGE}

DATE_DEPLOY=$(date --rfc-3339=seconds)
cat >>${SERVER_DIR_OF_ONE_SERVER}/history.txt <<.EOF
==================================
HOSTNAME = ${HOSTNAME}
DATE_DEPLOY = ${DATE_DEPLOY}
REPO_NAME = ${REPO_NAME}
PORT = ${PORT}
.EOF
