#!/bin/bash

for DATABASE_NAME in $DATABASES
do
    isServerNameValid $DATABASE_NAME
done

CONTAINER=${INAME}-db-server

DOCKERRM=$(docker rm ${CONTAINER} 2>/dev/null)
case "$DOCKERRM" in
    '') echo "found no old container '${CONTAINER}' to remove. That is ok" ;;
    * ) echo "removed old container '${CONTAINER}'" ;;
esac

echo "starting the database image openroberta/db_server:2.4.0 as '${CONTAINER}' for the database/s $DATABASES"
DOCKERID=$(docker run -d --name=${CONTAINER} \
                  --network $DOCKER_NETWORK_NAME \
                  -v ${DATABASE_DIR}:/opt/db \
                  -v $DB_ADMIN_DIR:/opt/dbAdmin \
                  -p $DATABASE_SERVER_PORT:9001 \
                  openroberta/db_server:2.4.0 $DATABASEXMX $DATABASES)
echo "database container started with id $DOCKERID"
