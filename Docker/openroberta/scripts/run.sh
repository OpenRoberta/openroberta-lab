#!/bin/bash

# script to generate, start, stop docker container (database and jetty) for openroberta. See Docker/openroberta/_README-standalone-1server.md for details

QUIET=false
YES=false
DEBUG=false

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/.. && pwd )"
SCRIPT_MAIN="${BASE_DIR}/scripts"
SCRIPT_HELPER="${SCRIPT_MAIN}/helper"
SCRIPT_REPORTING="${SCRIPT_MAIN}/reporting"

CMD=$1; shift
while [  1 ]
do
    case "${CMD}" in
        '-q')    CMD=$1; shift
                 QUIET='true' ;;
        '-yes')  CMD=$1; shift
                 YES='true' ;;
        '-D')    CMD=$1; shift
                 DEBUG='true' ;;
        *)       break ;;
    esac
done

if [ "${QUIET}" != 'true' ]
then
    source ${SCRIPT_HELPER}/_help.sh
fi

source ${BASE_DIR}/decl.sh
source ${SCRIPT_HELPER}/__defs.sh

case "${CMD}" in
    auto-deploy)        : ;;
    show-activity-once) : ;;
    *)            echo "${STAR_DATE_STAR}" ;;
esac

[ "${DEBUG}" = 'true' ] && echo "${DATE}: executing command '${CMD}'"
case "${CMD}" in
    help)         [ "${QUIET}" == true ] && source ${SCRIPT_HELPER}/_help.sh ;;
    gen)          SERVER_NAME=$1; shift
                  isServerNameValid ${SERVER_NAME}
                  headerMessage "generating image for server ${SERVER_NAME}"
                  source ${SCRIPT_HELPER}/_gen.sh
                  headerMessage "generating the image for server ${SERVER_NAME} finished" ;;
    from-hub)     SERVER_NAME=$1; shift
                  REPO_NAME=$1; shift
                  isServerNameValid ${SERVER_NAME}
                  question "pulling from hub STOPs the server ${SERVER_NAME} first"
                  headerMessage "pulling image for server ${SERVER_NAME} from a hub"
                  source ${SCRIPT_HELPER}/_stop.sh
                  source ${SCRIPT_HELPER}/_fromHub.sh
                  headerMessage "pulling the image for server ${SERVER_NAME} from a hub finished. YOU have to start the server!" ;;
    start)        SERVER_NAME=$1; shift
                  REMOTE_DEBUG=$1; shift
                  case ${REMOTE_DEBUG} in
                    '-rdbg') echo 'remote debugging uses tcp port 2000 LISTENing'
                             REMOTE_DEBUG=true ;;
                    *)       REMOTE_DEBUG=false ;;
                  esac
                  source ${SCRIPT_HELPER}/_stop.sh
                  source ${SCRIPT_HELPER}/_start.sh ;;
    stop)         SERVER_NAME=$1
                  source ${SCRIPT_HELPER}/_stop.sh ;;
    deploy)       SERVER_NAME=$1; shift
                  isServerNameValid ${SERVER_NAME}
                  headerMessage "deploying (stopping,generating,starting) the server '${SERVER_NAME}'"
                  source ${SCRIPT_HELPER}/_stop.sh
                  source ${SCRIPT_HELPER}/_gen.sh
                  source ${SCRIPT_HELPER}/_start.sh
                  headerMessage "deploying (stopping,generating,starting) the server '${SERVER_NAME}' finished" ;;
    admin)        SERVER_NAME=$1; shift
                  ADMIN_CMD=$1; shift
                  source ${SCRIPT_HELPER}/_containerAdmin.sh ;;
    auto-restart) SERVER_NAME=$1; shift; SERVER_URL=$1; shift; SLEEP_BETWEEN_CHECKS=$1; shift; SLEEP_TO_AVOID_FALSE_POSITIVES=$1; shift
                  source ${SCRIPT_HELPER}/_autorestart.sh ;;
    auto-deploy)  source ${SCRIPT_HELPER}/_autodeploy.sh ;;
    start-all)    echo 'start database container and all server container'
                  ${SCRIPT_MAIN}/run.sh -q start-dbc
                  sleep 60
                  for SERVER_NAME in ${SERVERS}; do
                      ${SCRIPT_MAIN}/run.sh -q start ${SERVER_NAME}
                  done ;;
    stop-all)     echo 'stop database container and all server container'
                  for SERVER_NAME in ${SERVERS}; do
                      ${SCRIPT_MAIN}/run.sh -q stop ${SERVER_NAME}
                  done
                  ${SCRIPT_MAIN}/run.sh -q stop-dbc ;;
    gen-net)      question "do you have double-checked, that the bridge network name '${DOCKER_NETWORK_NAME}' is NOT used elsewhere?"
                  echo "${DATE}: generating the openroberta bridge network '${DOCKER_NETWORK_NAME}'"
                  docker network create --driver bridge ${DOCKER_NETWORK_NAME}
                  echo "generating the openroberta bridge network '${DOCKER_NETWORK_NAME}' finished" ;;
    gen-dbc)      source ${SCRIPT_HELPER}/_dbContainerGen.sh ;;
    start-dbc)    source ${SCRIPT_HELPER}/_dbContainerStop.sh
                  source ${SCRIPT_HELPER}/_dbContainerStart.sh ;;
    stop-dbc)     source ${SCRIPT_HELPER}/_dbContainerStop.sh ;;
    backup)       DATABASE_NAME=$1
                  source ${SCRIPT_HELPER}/_dbContainerBackup.sh ;;
    backup-save)  FROM_PATH=$1; TO_PATH=$2
                  source ${SCRIPT_HELPER}/_dbBackupSave.sh ;;
    cleanup-temp-user-dirs)
                  SERVER_NAME=$1; shift
                  HOURS=$1; shift
                  source ${SCRIPT_HELPER}/_cleanupTempUserDirs.sh ;;
    network)      headerMessage "network inspect"
                  docker network inspect ${DOCKER_NETWORK_NAME} ;;
    docker-info)  headerMessage "system df"
                  docker system df
                  headerMessage "all images"
                  docker images
                  headerMessage "all container"
                  docker ps -a ;;
    logs)         set $(docker ps --format "{{.Names}}")
                  for NAME do
                      headerMessage "${NAME}"
                      docker logs --tail 10 ${NAME}
                  done ;;
    test-info)    cat ${BASE_DIR}/decl.sh
                  for SERVER_NAME in ${SERVERS}
                  do
                      headerMessage "decl.sh of server ${SERVER_NAME}"
                      cat ${SERVER_DIR}/${SERVER_NAME}/decl.sh
                  done ;;
    prune)        headerMessage "removing all exited container"
                  docker rm $(docker ps -q -f status=exited)
                  headerMessage "removing stale volumes"
                  docker volume rm $(docker volume ls -q -f dangling=true)
                  headerMessage "remove unused containers, networks, images"
                  docker system prune --force ;;
    monthly-stat) SERVER_NAME=$1; shift; MONTH=$1; shift; YEAR=$1; shift
                  source ${SCRIPT_HELPER}/_monthlyStat.sh ;;
    alive)        case ${ALIVE_ACTIVE} in
                      true) : ;;
                      *)    echo "variable ALIVE_ACTIVE not true. Functionality not available. This is no error. Exit 0"
                            exit 0 ;;
                  esac
                 
                  LAB_URL="$1"; shift
                  REPORT_ALWAYS=true
                  REPORT_MESSAGE="checked from host ${HOSTNAME}"
                  while [ "$1" != '' ]
                  do
                      case "$1" in
                          mail=always) REPORT_ALWAYS=true ;;
                          mail=error)  REPORT_ALWAYS=false ;;
                          msg=*)       REPORT_MESSAGE="${REPORT_MESSAGE}. ${1:4}" ;;
                      esac
                      shift
                  done
                  source ${SCRIPT_HELPER}/_alive.sh ;;

    show-server)         source ${SCRIPT_HELPER}/_serverStateOverview.sh ;;
    show-resources)      export SERVER_NAME=$1; shift
                         export LOGFILE=$1; shift
                         export LOWERLIMIT=$1; shift
                         export UPPERLIMIT=$1; shift
                         export SERVERURL=$1; shift
                         export DURATION=$1; shift
                         export PID=$1; shift
                         source ${SCRIPT_HELPER}/_showResources.sh ;;
    show-activity-once)  SERVER_NAME=$1; shift
                         LOGFILE=$1; shift
                         SERVERURL=$1; shift
                         source ${SCRIPT_HELPER}/_showActivityOnce.sh ;;
    gen-ccbin)           export ARCH=$1; shift
                         export CCBIN_VERSION=$1; shift
                         question 'you generate a new ccbin version. That is very seldom required. Is this ok?'
                         question 'really?'
                         cd ${BASE_DIR}/conf/${ARCH}/1-cc-binaries
                         docker build --no-cache -t openroberta/ccbin-${ARCH}:${CCBIN_VERSION} .
                         question "you generated an image openroberta/ccbin-${ARCH}:${CCBIN_VERSION}. Should that image be pushed to dockerhub?"
                         docker push openroberta/ccbin-${ARCH}:${CCBIN_VERSION} ;;
    gen-base)            export ARCH=$1; shift
                         export CCBIN_VERSION=$1; shift
                         export ORACCRSC_TAG=$1; shift
                         export BASE_VERSION=$1; shift
                         export CC_RESOURCES=$1; shift
                         if [ "${ORACCRSC_TAG}" != "${BASE_VERSION}" ]; then
                             question "oraccrsc tag (${ORACCRSC_TAG}) not equal to base version (${BASE_VERSION}). Makes sense for tests. Is this ok?"
                         fi
                         question 'you generated a new ccbase version. That is not often required. Is this ok?'
                         question 'is the ora-cc-rsc repo given as parameter clean? Can arbitrary tags be checked out?'
                         cd $CC_RESOURCES
                         if [ ! -d .git ]; then echo "this script only runs in a git directory - exit 12"; exit 12; fi

                         git fetch --all; git reset --hard; git clean -fd
                         git checkout master; git pull
                         git checkout tags/${BASE_VERSION}

                         mvn clean install    # necessary to create the update resources for ev3- and arduino-based systems
                         docker build --no-cache -t openroberta/base-${ARCH}:${BASE_VERSION} \
                                --build-arg CCBIN_VERSION=${CCBIN_VERSION} \
                                -f $BASE_DIR/conf/${ARCH}/2-cc-resources/Dockerfile .
                         question "you generated an image openroberta/base-${ARCH}:${BASE_VERSION}. Should that image be pushed to dockerhub?"
                         docker push openroberta/base-${ARCH}:${BASE_VERSION}
                         question "should the image openroberta/base-${ARCH} become the 'latest' tag at dockerhub?"
                         docker tag openroberta/base-${ARCH}:${BASE_VERSION} openroberta/base-${ARCH}:latest
                         docker push openroberta/base-${ARCH}:latest ;;

    test)                source ${SCRIPT_HELPER}/_test.sh ;;
    *)                   echo "invalid command: '${CMD}'" ;;
esac
