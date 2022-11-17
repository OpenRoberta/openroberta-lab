#!/bin/bash

IMAGE="openroberta/db_server_${ARCH}:${HSQL_DB_SERVER_VERSION}"

echo "${DATE}: generating the database image '${IMAGE}'"
docker build --no-cache -f ${CONF_DIR}/y-docker-for-db/Dockerfile -t ${IMAGE} ${CONF_DIR}/y-docker-for-db
echo "generating the database image '${IMAGE}' finished"