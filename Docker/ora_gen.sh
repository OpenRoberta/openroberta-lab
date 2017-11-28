#!/bin/bash

if [[ ! $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container :-)'
else
   echo 'not running in a docker container - exit 1 to avoid destruction and crash :-)'
   exit 1
fi
VERSION="$1"
cd /opt
wget -q https://github.com/OpenRoberta/robertalab/archive/develop.zip && \
     unzip develop.zip && \
     rm develop.zip

cd /opt/robertalab-develop/OpenRobertaParent
mvn clean install -DskipTests -DskipITs
cd /opt/robertalab-develop
rm -rf DockerInstallation/*
./ora.sh --export DockerInstallation
cp Docker/DockerfileEmbedded DockerInstallation/Dockerfile
cd /opt/robertalab-develop/DockerInstallation
docker build -t rbudde/openrobertalab:$VERSION .
