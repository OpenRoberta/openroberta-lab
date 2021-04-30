#!/bin/bash

if [[ ! $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container :-)'
else
   echo 'not running in a docker container - exit 12 to avoid destruction and crash :-)'
   exit 12
fi
GITREPO=$1
case "$GITREPO" in
    https:*) : ;;
    *)       echo 'the git repo url is missing or invalid (first parameter) - exit 12'
             exit 12 ;;
esac
BRANCH=$2
if [ -z "$BRANCH" ]
then
    echo 'the branch to build is missing (second parameter) - exit 12'
    exit 12
fi

cd /opt
git clone --depth=1 "$GITREPO" -b "$BRANCH"
cd /opt/openroberta-lab
mvn clean install -DskipTests -DskipITs

# execute all tests, including the integration tests
mvn install -PrunIT
RC=$?
echo "maven return code is $RC"
 
case $RC in
  0) echo "returning SUCCESS"
     exit 0 ;;
  *) echo "returning ERROR"
     exit 16 ;;
esac