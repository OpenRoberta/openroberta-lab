#!/bin/bash

DBDIR=$BASE/db
isDirectoryValid $DBDIR

if [ -f $DBDIR/databases.txt ]
then
    DATABASES=$(cat $DBDIR/databases.txt)
    set $DATABASES
    for DB_NAME do
        isServerNameValid $DB_NAME
    done    
else
    echo "file 'databases.txt' is missing. Exit 12"
    exit 12
fi

echo "starting the database image rbudde/openroberta_db:2.4.0 for the databases $*"
DOCKERID=$(docker run -d --name=ora-db-server --network ora-net -v $BASE/db:/opt/db -p 9001:9001 rbudde/openroberta_db:2.4.0 $DATABASES)
echo "database container started with id $DOCKERID"
