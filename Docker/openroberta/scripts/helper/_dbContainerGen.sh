#!/bin/bash

IMAGE="openroberta/db_server_${ARCH}:2.4.0"

echo "${DATE}: generating the database image '${IMAGE}'"
docker build --no-cache -f ${CONF_DIR}/y-docker-for-db/Dockerfile -t ${IMAGE} ${CONF_DIR}/y-docker-for-db
echo "generating the database image '${IMAGE}' finished"