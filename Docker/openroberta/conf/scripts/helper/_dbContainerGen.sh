#!/bin/bash

echo "$DATE: generating the database image rbudde/openroberta_db:2.4.0"
docker build --no-cache -f ${CONF_DIR}/docker-for-db/DockerfileDb -t rbudde/openroberta_db_server:2.4.0 ${CONF_DIR}/docker-for-db
echo "generating the database image rbudde/openroberta_db:2.4.0 finished"