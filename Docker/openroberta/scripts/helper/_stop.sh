#!/bin/bash

isServerNameValid ${SERVER_NAME}

case "${SERVER_NAME}" in
    master) question 'do you really want to stop the running container with the MASTER server?'
            question 'you know, that this may stop the PROD system?' ;;
     *)     : ;;
esac

CONTAINER="${INAME}-${SERVER_NAME}"
echo "stopping the server container '${CONTAINER}', if it is running"
DOCKERSTOP=$(docker stop ${CONTAINER} 2>/dev/null)
case "$DOCKERSTOP" in
    '') echo "found no container '${CONTAINER}' to stop. That is ok" ;;
    * ) echo "stopped container '$DOCKERSTOP'" ;;
esac
case "${SERVER_NAME}" in
    master) echo "the master container '${CONTAINER}' is not removed to allow error analysis" ;;
     *)     DOCKERRM=$(docker rm ${CONTAINER} 2>/dev/null)
            case "$DOCKERRM" in
                '') echo "found no container '${CONTAINER}' to remove. That is ok" ;;
                * ) echo "removed container '$DOCKERRM'" ;;
            esac ;;
esac