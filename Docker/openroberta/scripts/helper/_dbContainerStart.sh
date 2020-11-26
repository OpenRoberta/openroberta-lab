#!/bin/bash

for DATABASE_NAME in ${DATABASES}
do
    isServerNameValid ${DATABASE_NAME}
done

IMAGE="openroberta/db_server_${ARCH}:2.4.0"
CONTAINER=db-server

DOCKERRM=$(docker rm ${CONTAINER} 2>/dev/null)
case "${DOCKERRM}" in
    '') echo "found no old container '${CONTAINER}' to remove. That is ok" ;;
    * ) echo "removed old container '${CONTAINER}'" ;;
esac

echo "starting the database image '${IMAGE}' as '${CONTAINER}' for the database/s ${DATABASES}"
if $(isWin)
then
    export MSYS_NO_PATHCONV=1
fi
DOCKERID=$(docker run -d --name=${CONTAINER} \
                  --network ${DOCKER_NETWORK_NAME} \
                  -v ${DATABASE_DIR}:/opt/db \
                  -v ${DB_ADMIN_DIR}:/opt/dbAdmin \
                  -p ${DATABASE_SERVER_PORT}:9001 \
                  ${IMAGE} ${DATABASEXMX} ${DATABASES})
echo "database container started with id ${DOCKERID}"
