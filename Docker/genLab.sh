#!/bin/bash

if [[ ! $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container :-)'
else
   echo 'not running in a docker container - exit 1 to avoid destruction and crash :-)'
   exit 1
fi
BRANCH=$1
if [ -z "$BRANCH" ]
then
    echo 'the branch to build is missing (first parameter) - exit 12'
    exit 12
fi
VERSION="$2"
if [ -z "$VERSION" ]
then
    echo 'the version parameter of form x.y.z is missing (second parameter) - exit 12'
    exit 12
fi
BUILD_ALL="$3"
if [ -z "$BUILD_ALL" ]
then
    BUILD_ALL='true'
elif [ "$BUILD_ALL" != 'false' ]
then
    BUILD_ALL='true'
fi
echo "building branch $BRANCH with version $VERSION. Build all container is set to $BUILD_ALL"
git pull
git checkout $BRANCH
cd /opt/robertalab/OpenRobertaParent
mvn clean install
chmod +x RobotArdu/resources/linux/arduino-builder RobotArdu/resources/linux/tools-builder/ctags/5.8*/ctags

cd /opt/robertalab
rm -rf DockerInstallation
./ora.sh --export /opt/robertalab/DockerInstallation

cp Docker/Dockerfile* Docker/*.sh DockerInstallation

cd /opt/robertalab/DockerInstallation

LAST_COMMIT=$(git rev-list HEAD...HEAD~1)
DOCKER_VERSION=$BRANCH-$VERSION
PREFIX=rbudde/openroberta

docker build -t ${PREFIX}_lab:$DOCKER_VERSION -t ${PREFIX}_lab:$LAST_COMMIT -f DockerfileLab .
if [ "$BUILD_ALL" == 'true' ]
then
    docker build -t ${PREFIX}_db:$BRANCH-$VERSION       -f DockerfileDb --build-arg version=$VERSION .
    docker build -t ${PREFIX}_upgrade:$BRANCH-$VERSION  -f DockerfileUpgrade                         .
    docker build -t ${PREFIX}_embedded:$BRANCH-$VERSION -f DockerfileLabEmbedded                     .
fi