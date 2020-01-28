#!/bin/bash

# script to generate, start, stop docker container (database and jetty) for openroberta. See Docker/openroberta/_README.md for details

QUIET=false
YES=false
DEBUG=false

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/.. && pwd )"
SCRIPT_MAIN="${BASE_DIR}/scripts"
SCRIPT_HELPER="${SCRIPT_MAIN}/helper"
SCRIPT_REPORTING="${SCRIPT_MAIN}/reporting"
SCRIPT_ANALYSIS="${SCRIPT_MAIN}/analysis"

CMD=$1; shift
while [  1 ]
do
    case "$CMD" in
        '-q')   CMD=$1; shift
                QUIET='true' ;;
        '-yes') CMD=$1; shift
                YES='true' ;;
        '-D')   CMD=$1; shift
                DEBUG='true' ;;
        *)      break ;;
    esac
done

if [ "$QUIET" != 'true' ]
then
    source ${SCRIPT_HELPER}/_help.sh
fi

source ${BASE_DIR}/decl.sh
source ${SCRIPT_HELPER}/__defs.sh

DATE=$(date '+%Y-%m-%d %H:%M:%S')

[ "$DEBUG" = 'true' ] && echo "$DATE: executing command '$CMD'"
case "$CMD" in
    help)         [ "$QUIET" == true ] && source ${SCRIPT_HELPER}/_help.sh ;;
    gen)          SERVER_NAME=$1; shift
                  isServerNameValid ${SERVER_NAME}
                  echo "$DATE: generating the server '${SERVER_NAME}'"
                  source ${SCRIPT_HELPER}/_gen.sh
                  echo "generating the server '${SERVER_NAME}' finished" ;;
    start)        SERVER_NAME=$1; shift
                  OPTIONAL_VERSION=$1; shift
                  source ${SCRIPT_HELPER}/_stop.sh
                  source ${SCRIPT_HELPER}/_start.sh ;;
    start-export) SERVER_NAME=$1; shift
                  source ${SCRIPT_HELPER}/_startExport.sh ;;
    stop)         SERVER_NAME=$1
                  source ${SCRIPT_HELPER}/_stop.sh ;;
    deploy)       SERVER_NAME=$1; shift
                  echo "$DATE: deploying (generating,starting) the server '${SERVER_NAME}'"
                  ${SCRIPT_MAIN}/run.sh -q gen ${SERVER_NAME}
                  ${SCRIPT_MAIN}/run.sh -q start ${SERVER_NAME} ;;
    admin)        SERVER_NAME=$1; shift
                  ADMIN_CMD=$1; shift
                  source ${SCRIPT_HELPER}/_containerAdmin.sh ;;
    auto-restart) SERVER_NAME=$1; shift; SERVER_URL=$1; shift
                  source ${SCRIPT_HELPER}/_autorestart.sh ;;
    auto-deploy)  source ${SCRIPT_HELPER}/_autodeploy.sh ;;
    start-all)    echo '******************** '$DATE' ********************'
                  echo 'start database container and all server container'
                  ${SCRIPT_MAIN}/run.sh -q start-dbc
                  sleep 10
                  for SERVER_NAME in $SERVERS; do
                      ${SCRIPT_MAIN}/run.sh -q start ${SERVER_NAME}
                  done ;;
    stop-all)     echo '******************** '$DATE' ********************'
                  echo 'stop database container and all server container'
                  for SERVER_NAME in $SERVERS; do
                      ${SCRIPT_MAIN}/run.sh -q stop ${SERVER_NAME}
                  done
                  ${SCRIPT_MAIN}/run.sh -q stop-dbc ;;
    gen-net)      question "do you have double-checked, that the bridge network name '$DOCKER_NETWORK_NAME' is NOT used elsewhere?"
                  echo "$DATE: generating the openroberta bridge network '$DOCKER_NETWORK_NAME'"
                  docker network create --driver bridge $DOCKER_NETWORK_NAME
                  echo "generating the openroberta bridge network '$DOCKER_NETWORK_NAME' finished" ;;
    gen-dbc)      source ${SCRIPT_HELPER}/_dbContainerGen.sh ;;
    start-dbc)    source ${SCRIPT_HELPER}/_dbContainerStop.sh
                  source ${SCRIPT_HELPER}/_dbContainerStart.sh ;;
    stop-dbc)     source ${SCRIPT_HELPER}/_dbContainerStop.sh ;;
    backup)       DATABASE_NAME=$1
                  source ${SCRIPT_HELPER}/_dbContainerBackup.sh ;;
    backup-save)  FROM_PATH=$1; TO_PATH=$2
                  source ${SCRIPT_HELPER}/_dbBackupSave.sh ;;
    network)      echo '******************** '$DATE' ********************'
                  echo '******************** network inspect'
                  docker network inspect $DOCKER_NETWORK_NAME ;;
    docker-info)  echo '******************** '$DATE' ********************'
                  echo '******************** system df'
                  docker system df
                  echo '******************** all images'
                  docker images
                  echo '******************** all container'
                  docker ps -a ;;
    logs)         echo '******************** '$DATE' ********************'
                  set $(docker ps --format "{{.Names}}")
                  for NAME do
                      echo "******************** $NAME"
                      docker logs --tail 10 $NAME
                  done ;;
    test-info)    echo '******************** '$DATE' ********************'
                  cat ${BASE_DIR}/decl.sh
                  for SERVER_NAME in $SERVERS
                  do
                      echo "******************** decl.sh of server ${SERVER_NAME}"
                      cat ${SERVER_DIR}/${SERVER_NAME}/decl.sh
                  done ;;
    prune)        echo '******************** '$DATE' ********************'
                  echo '******************** removing all exited container ********************'
                  docker rm $(docker ps -q -f status=exited)
                  echo '******************** removing stale volumes ********************'
                  docker volume rm $(docker volume ls -q -f dangling=true)
                  echo '******************** remove unused containers, networks, images ********************'
                  docker system prune --force ;;
    show-server)  ${SCRIPT_ANALYSIS}/serverStateOverview.sh ;;
    show-resources) export SERVER_NAME=$1; shift
                  export LOGFILE=$1; shift
                  export LOWERLIMIT=$1; shift
                  export UPPERLIMIT=$1; shift
                  export SERVERURL=$1; shift
                  export DURATION=$1; shift
                  export PID=$1; shift
                  source ${SCRIPT_ANALYSIS}/showResources.sh ;;
    monthly-stat) SERVER_NAME=$1; shift; MONTH=$1; shift
                  source ${SCRIPT_HELPER}/_monthlyStat.sh ;;
    alive)        case $ALIVE_ACTIVE in
                      true) : ;;
                      *)    echo "variable ALIVE_ACTIVE not true. Functionality not available. Exit 12"
                            exit 12 ;;
                  esac
                 
                  LAB_URL="$1"; shift
                  REPORT_ALWAYS=true
                  REPORT_MESSAGE="checked from host $HOSTNAME"
                  while [ "$1" != '' ]
                  do
                      case "$1" in
                          mail=always) REPORT_ALWAYS=true ;;
                          mail=error)  REPORT_ALWAYS=false ;;
                          msg=*)       SPLIT=(${1/=/ }); REPORT_MESSAGE=${SPLIT[1]} ;;
                      esac
                      shift
                  done
                  source ${SCRIPT_HELPER}/_alive.sh ;;
    test)         echo '******************** TEST MODE START ********************'
                  source ${SCRIPT_HELPER}/_test.sh
                  echo '******************** TEST MODE TERMINATED ***************' ;;
    *)            echo "$DATE: invalid command: '$CMD'" ;;
esac
