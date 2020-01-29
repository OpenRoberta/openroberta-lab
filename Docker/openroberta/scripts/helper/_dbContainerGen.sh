#!/bin/bash

echo "$DATE: generating the database image openroberta/db_server:2.4.0"
docker build --no-cache -f ${CONF_DIR}/docker-for-db/DockerfileDb -t openroberta/db_server:2.4.0 ${CONF_DIR}/docker-for-db
echo "generating the database image openroberta/db_server:2.4.0 finished"