#!/bin/bash

if [ ! -f $DATABASE_DIR/databases.txt ]
then
    echo "file 'databases.txt' is missing. Exit 12"
    exit 12
fi

DATABASE_NAMES=$(cat $DATABASE_DIR/databases.txt)
set $DATABASE_NAMES
for DATABASE_NAME do
    isServerNameValid $DATABASE_NAME
done    
echo "first removing the old database server container - hopefully you saved the logging"
docker rm ora-db-server >>/dev/null
echo "starting the database image rbudde/openroberta_db:2.4.0 for the databases $*"
DOCKERID=$(docker run -d --name=ora-db-server \
                  --network ora-net \
                  -v $DATABASE_DIR:/opt/db \
                  -v $DB_ADMIN_DIR:/opt/dbAdmin \
                  -p $DATABASE_SERVER_PORT:9001 \
                  rbudde/openroberta_db:2.4.0 $DATABASE_NAMES)
echo "database container started with id $DOCKERID"
