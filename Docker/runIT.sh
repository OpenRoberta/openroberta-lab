#!/bin/bash

if [[ ! $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container :-)'
else
   echo 'not running in a docker container - exit 1 to avoid destruction and crash :-)'
   exit 1
fi
BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo "operating on branch $BRANCH"
git pull --depth=1

cd /opt/robertalab/OpenRobertaParent
mvn clean install -DskipTests -DskipITs

# execute all tests, including the integration tests
mvn install -Pdebug,runIT
RC=$?
echo "maven return code is $RC"
 
case $RC in
  0) echo "returning SUCCESS"
     exit 0 ;;
  *) echo "returning ERROR"
     exit 16 ;;
esac