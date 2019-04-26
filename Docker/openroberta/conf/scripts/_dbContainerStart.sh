#!/bin/bash

if [ -f $DATABASE_DIR/databases.txt ]
then
    DATABASE_NAMES=$(cat $DATABASE_DIR/databases.txt)
    set $DATABASE_NAMES
    for DATABASE_NAME do
        isServerNameValid $DATABASE_NAME
    done    
else
    echo "file 'databases.txt' is missing. Exit 12"
    exit 12
fi

echo "starting the database image rbudde/openroberta_db:2.4.0 for the databases $*"
DOCKERID=$(docker run -d --name=ora-db-server --network ora-net -v $DATABASE_DIR:/opt/db -p $DATABASE_SERVER_PORT:9001 rbudde/openroberta_db:2.4.0 $DATABASE_NAMES)
echo "database container started with id $DOCKERID"
