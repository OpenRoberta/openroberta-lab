#!/bin/bash

# script to generate, start, stop docker container (database and jetty) for openroberta. See Docker/openroberta/_README.md for details

DEBUG=false

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/../.. && pwd )"
SCRIPT_MAIN="${BASE_DIR}/conf/scripts"
SCRIPT_HELPER="${SCRIPT_MAIN}/helper"

CMD=$1; shift
if [ "$CMD" == '-q' ]
then
    CMD=$1; shift
    QUIET='true'
else
    source ${SCRIPT_HELPER}/_help.sh
fi
if [ "$CMD" == '-D' ]
then
    CMD=$1; shift
    DEBUG=true
fi

source ${BASE_DIR}/config.sh
source ${SCRIPT_HELPER}/__defs.sh

DATE=$(date '+%Y-%m-%d %H:%M:%S')

[ "$DEBUG" = 'true' ] && echo "$DATE: executing command '$CMD'"
case "$CMD" in
    help)        [ "$QUIET" == true ] && source ${SCRIPT_HELPER}/_help.sh ;;
    gen)         SERVER_NAME=$1; shift
                 isServerNameValid ${SERVER_NAME}
                 echo "$DATE: generating the server '${SERVER_NAME}'"
                 source ${SCRIPT_HELPER}/_gen.sh
                 echo "generating the server '${SERVER_NAME}' finished" ;;
    start)       SERVER_NAME=$1; shift
                 source ${SCRIPT_HELPER}/_stop.sh
                 source ${SCRIPT_HELPER}/_start.sh ;;
    stop)        SERVER_NAME=$1
                 source ${SCRIPT_HELPER}/_stop.sh ;;
    deploy)      SERVER_NAME=$1; shift
                 echo "$DATE: deploying (generating,starting) the server '${SERVER_NAME}'"
                 ${SCRIPT_MAIN}/run.sh -q gen ${SERVER_NAME}
                 ${SCRIPT_MAIN}/run.sh -q start ${SERVER_NAME} ;;
    admin)       SERVER_NAME=$1; shift
                 ADMIN_CMD=$1; shift
                 source ${SCRIPT_HELPER}/_containerAdmin.sh ;;
    auto-deploy) source ${SCRIPT_HELPER}/_autodeploy.sh ;;
    start-all)   echo '******************** '$DATE' ********************'
                 echo 'start database container and all server container'
                 ${SCRIPT_MAIN}/run.sh -q start-dbc
                 sleep 10
                 for SERVER_NAME in $SERVERS; do
                     ${SCRIPT_MAIN}/run.sh -q start ${SERVER_NAME}
                 done ;;
    stop-all)  echo '******************** '$DATE' ********************'
               echo 'stop database container and all server container'
               for SERVER_NAME in $SERVERS; do
                   ${SCRIPT_MAIN}/run.sh -q stop ${SERVER_NAME}
               done
               ${SCRIPT_MAIN}/run.sh -q stop-dbc ;;
    gen-net)   question "do you have double-checked, that the bridge network name '$DOCKER_NETWORK_NAME' is NOT used elsewhere?"
               question 'really?'
               echo "$DATE: generating the openroberta bridge network '$DOCKER_NETWORK_NAME'"
               docker network create --driver bridge $DOCKER_NETWORK_NAME
               echo "generating the openroberta bridge network '$DOCKER_NETWORK_NAME' finished" ;;
    gen-dbc)   source ${SCRIPT_HELPER}/_dbContainerGen.sh ;;
    start-dbc) source ${SCRIPT_HELPER}/_dbContainerStop.sh
               source ${SCRIPT_HELPER}/_dbContainerStart.sh ;;
    stop-dbc)  source ${SCRIPT_HELPER}/_dbContainerStop.sh ;;
    backup)    DATABASE_NAME=$1
               source ${SCRIPT_HELPER}/_dbContainerBackup.sh ;;
    network)   echo '******************** '$DATE' ********************'
               echo '******************** network inspect'
               docker network inspect $DOCKER_NETWORK_NAME ;;
    docker-info) echo '******************** '$DATE' ********************'
               echo '******************** system df'
               docker system df
               echo '******************** all images'
               docker images
               echo '******************** all container'
               docker ps -a ;;
    logs)      echo '******************** '$DATE' ********************'
               set $(docker ps --format "{{.Names}}")
               for NAME do
                   echo "******************** $NAME"
                   docker logs --tail 10 $NAME
               done ;;
    test-info) echo '******************** '$DATE' ********************'
               cat ${BASE_DIR}/config.sh
               for SERVER_NAME in $SERVERS
               do
                   echo "******************** decl.sh of server ${SERVER_NAME}"
                   cat ${SERVER_DIR}/${SERVER_NAME}/decl.sh
               done ;;
    prune)     echo '******************** '$DATE' ********************'
               echo '******************** removing all exited container ********************'
               docker rm $(docker ps -q -f status=exited)
               echo '******************** removing stale volumes ********************'
               docker volume rm $(docker volume ls -q -f dangling=true)
               echo '******************** remove unused containers, networks, images ********************'
               docker system prune --force ;;
    alive)     LAB_URL="$1"; shift
               REPORT_ALWAYS=true
               case "$1" in
                 false) REPORT_ALWAYS=false ;;
               esac
               source ${SCRIPT_HELPER}/_alive.sh ;;
    test)      echo '******************** TEST MODE START ********************'
               source ${SCRIPT_HELPER}/_test.sh
               echo '******************** TEST MODE TERMINATED ***************' ;;
    *)         echo "$DATE: invalid command: '$CMD'" ;;
esac
