#!/bin/bash

echo "$0: help | gen <server> | start <server> | stop <server> | deploy <server> |genDbC | startDbC <db-name-1> ... <db-name-n> | info | logs | prune"

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
chmod ugo+rx $SCRIPTDIR/run.sh

source $SCRIPTDIR/__defs.sh

CMD=$1; shift
case "$CMD" in
    help)     : ;;
    gen)      SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              echo "generating the server '$SERVER_NAME'"
              source $SCRIPTDIR/_generate.sh
              echo "generating the server '$SERVER_NAME' finished" ;;
    start)    SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              echo "first trying to stop the server '$SERVER_NAME'"
              source $SCRIPTDIR/_stop.sh
              echo "starting the server '$SERVER_NAME'"
              source $SCRIPTDIR/_start.sh
              echo "starting the server '$SERVER_NAME' finished" ;;
    stop)     SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              echo "stopping the server '$SERVER_NAME'"
              source $SCRIPTDIR/_stop.sh
              echo "stopping the server '$SERVER_NAME' finished" ;;
    deploy)   SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              echo "deploying (generating,starting) the server '$SERVER_NAME'"
              $SCRIPTDIR/run.sh gen $SERVER_NAME
              $SCRIPTDIR/run.sh start $SERVER_NAME ;;
    genDbC)   echo "generating the database container rbudde/openroberta_db:2.4.0"
              docker build -f $CONF/docker-for-db/DockerfileDb -t rbudde/openroberta_db:2.4.0 $CONF/docker-for-db
              echo "generating the database container rbudde/openroberta_db:2.4.0 finished" ;;
    startDbC) source $SCRIPTDIR/_startDbServer.sh ;;
    info)     echo '******************** system df ********************'
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
    *)        echo "invalid command: '$CMD'" ;;
esac
