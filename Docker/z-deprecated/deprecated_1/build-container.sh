#!/bin/bash
if [ $# -eq 0 ]
  then
    echo "Please provide the directory with a Dockerfile."
    exit 0
fi

if [ ! -d "$1" ]; then
  echo $1 "is not present."
  exit 0
fi

docker build -t robertalab/integration:v0.1-$1 -f $1/Dockerfile ..
