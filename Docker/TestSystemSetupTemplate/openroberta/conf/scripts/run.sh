#!/bin/bash

echo "$0: help | gen <server> | start <server> | stop <server> | deploy <server> | genNet | genDbC | startDbC | stopDbC | info | logs | prune"

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
              if [ "$SERVER_NAME" == '' ]
              then
                  if [ -f "$SERVER/servers.txt" ]
                  then
                      SERVERS=$(cat $SERVER/servers.txt)
                      set $SERVERS
                      for SERVER_NAME do
                          isServerNameValid $SERVER_NAME
                      done
                      for SERVER_NAME do
                          source $SCRIPTDIR/_stop.sh
                          source $SCRIPTDIR/_start.sh
                      done
                  else
                      echo "no file '$SERVER/servers.txt' found. Exit 12"
                      exit 12
                  fi
              else
                  isServerNameValid $SERVER_NAME
                  source $SCRIPTDIR/_stop.sh
                  source $SCRIPTDIR/_start.sh
              fi
              echo "finished" ;;
    stop)     SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              source $SCRIPTDIR/_stop.sh ;;
    deploy)   SERVER_NAME=$1; shift
              isServerNameValid $SERVER_NAME
              echo "deploying (generating,starting) the server '$SERVER_NAME'"
              $SCRIPTDIR/run.sh gen $SERVER_NAME
              $SCRIPTDIR/run.sh start $SERVER_NAME ;;
    genDbC)   echo "generating the database image rbudde/openroberta_db:2.4.0"
              docker build -f $CONF/docker-for-db/DockerfileDb -t rbudde/openroberta_db:2.4.0 $CONF/docker-for-db
              echo "generating the database image rbudde/openroberta_db:2.4.0 finished" ;;
    genNet)   echo "generating the openroberta bridge network 'ora-net'"
              docker network create --driver bridge ora-net
              echo "generating the openroberta bridge network 'ora-net' finished" ;;
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
    *)        echo "invalid command: '$CMD'" ;;
esac
