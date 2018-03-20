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
echo "building branch $BRANCH with version $VERSION"
git clone --depth=1 -b $BRANCH https://github.com/OpenRoberta/robertalab.git
cd /opt/robertalab/OpenRobertaParent
mvn clean install
chmod +x RobotArdu/resources/linux/arduino-builder RobotArdu/resources/linux/tools-builder/ctags/5.8*/ctags

cd /opt/robertalab
rm -rf DockerInstallation
./ora.sh --export /opt/robertalab/DockerInstallation

yes yes | ./ora.sh --createEmptydb
cp -r OpenRobertaParent/OpenRobertaServer/db-$VERSION DockerInstallation

cp Docker/Dockerfile* Docker/*.sh DockerInstallation

cd /opt/robertalab/DockerInstallation

docker build -t rbudde/openroberta_lab:$BRANCH-$VERSION            -f DockerfileLab                                         .
docker build -t rbudde/openroberta_db:$BRANCH-$VERSION             -f DockerfileDb             --build-arg version=$VERSION .

docker build -t rbudde/openroberta_upgrade:$BRANCH-$VERSION        -f DockerfileUpgrade                                     .

docker build -t rbudde/openroberta_embedded:$BRANCH-$VERSION       -f DockerfileLabEmbedded                                 .
docker build -t rbudde/openroberta_emptydbfortest:$BRANCH-$VERSION -f DockerfileDbEmptyForTest --build-arg version=$VERSION .