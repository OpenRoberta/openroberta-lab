#!/bin/bash

IMAGE="openroberta/db_server_${ARCH}:${HSQL_DB_SERVER_VERSION}"
ARTIFACT="org.hsqldb:hsqldb:${HSQL_DB_SERVER_VERSION}"
POM="hsqldb-${HSQL_DB_SERVER_VERSION}.pom"

# get the hsqldb with the correct version number
cd ${CONF_DIR}/y-docker-for-db
rm -f ${POM} hsqldb-*.jar
mvn dependency:copy -DoutputDirectory=. -Dartifact=${ARTIFACT}:pom
mvn dependency:copy -DoutputDirectory=. -f "$POM" -Dartifact=${ARTIFACT}
rm -f ${POM}

# build the image
echo "${DATE}: generating the database image '${IMAGE}'"
docker build --no-cache -f ${CONF_DIR}/y-docker-for-db/Dockerfile -t ${IMAGE} .

echo "generating the database image '${IMAGE}' finished"