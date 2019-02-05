#!/bin/bash

HELP_TEXT="$0: [-q] help | gen <server> | start [<server>] | stop [<server>] | deploy [<server>] | autoDeploy | restart | genNet | genDbC | startDbC | stopDbC | info | logs | prune"

CMD=$1; shift
if [ "$CMD" == '-q' ]
then
    CMD=$1; shift
    QUIET='true'
else
    echo "$HELP_TEXT"
fi

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
chmod ugo+rx $SCRIPTDIR/run.sh

source $SCRIPTDIR/__defs.sh

case "$CMD" in
    help)     if [ "$QUIET" == true ]
              then
                  echo "$HELP_TEXT"
              fi ;;
    gen)      SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              echo "generating the server '$SERVER_NAME'"
              source $SCRIPTDIR/_generate.sh
              echo "generating the server '$SERVER_NAME' finished" ;;
    start)    setServerNamesintoSERVER_NAMES $1
              for SERVER_NAME in $SERVER_NAMES; do
                  source $SCRIPTDIR/_stop.sh
                  source $SCRIPTDIR/_start.sh
              done ;;
    stop)     setServerNamesintoSERVER_NAMES $1
              for SERVER_NAME in $SERVER_NAMES; do
                  source $SCRIPTDIR/_stop.sh
              done ;;
    deploy)   setServerNamesintoSERVER_NAMES $1
              for SERVER_NAME in $SERVER_NAMES; do
                  echo "deploying (generating,starting) the server '$SERVER_NAME'"
                  $SCRIPTDIR/run.sh gen $SERVER_NAME
                  $SCRIPTDIR/run.sh start $SERVER_NAME
              done ;;
    autoDeploy) source $SCRIPTDIR/_autodeploy.sh ;;
    restart)  $SCRIPTDIR/run.sh startDbC
              sleep 10
              $SCRIPTDIR/run.sh start ;;
    genNet)   echo "generating the openroberta bridge network 'ora-net'"
              docker network create --driver bridge ora-net
              echo "generating the openroberta bridge network 'ora-net' finished" ;;
    genDbC)   echo "generating the database image rbudde/openroberta_db:2.4.0"
              docker build -f $CONF/docker-for-db/DockerfileDb -t rbudde/openroberta_db:2.4.0 $CONF/docker-for-db
              echo "generating the database image rbudde/openroberta_db:2.4.0 finished" ;;
    startDbC) source $SCRIPTDIR/_dbContainerStop.sh
              source $SCRIPTDIR/_dbContainerStart.sh ;;
    stopDbC)  source $SCRIPTDIR/_dbContainerStop.sh ;;
    info)     echo '******************** network inspect ********************'
              docker network inspect ora-net
              echo '******************** system df ********************'
              docker system df
              echo '******************** all images ********************'
              docker images
              echo '******************** all container ********************'
              docker ps -a ;;
    logs)     set $(docker ps --format "{{.Names}}")
              for NAME do
                  echo "******************** $NAME ********************"
                  docker logs --tail 10 $NAME
              done ;;
    prune)    echo '******************** removing all exited container ********************'
              docker rm $(docker ps -q -f status=exited)
              echo '******************** removing stale volumes ********************'
              docker volume rm $(docker volume ls -q -f dangling=true)
              echo '******************** remove unused containers, networks, images ********************'
              docker system prune ;;
    test)     echo '******************** TEST MODE START ********************'
              source $SCRIPTDIR/_test.sh
              echo '******************** TEST MODE TERMINATED ***************' ;;
    *)        echo "invalid command: '$CMD'" ;;
esac
