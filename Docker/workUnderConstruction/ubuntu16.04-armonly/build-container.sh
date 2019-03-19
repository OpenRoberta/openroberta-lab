#!/bin/bash
# sample run command:
# docker run --detach --rm --name calliope-arm-gcc -v /tmp:/tmp robertalab/crosscompilers:v0.1-ubuntu16.04-armonly
cp -r ../../OpenRobertaParent/RobotMbed/resources/ .
docker build -t robertalab/crosscompilers:v0.1-ubuntu16.04-armonly .
rm -r resources
