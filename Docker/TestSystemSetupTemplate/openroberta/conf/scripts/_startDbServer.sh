#!/bin/bash

if [ $# -le 0 ]
then
    echo "no database name given. Exit 12"
    exit 12
fi
for DB_NAME do
    isServerNameValid $DB_NAME
done
DBDIR=$BASE/db
isDirectoryValid $DBDIR

echo "starting the database server"

DOCKERID=$(cat $DBDIR/dockerid.txt)
if [ "$DOCKERID" != '' ]
then
    echo "trying to stop the running database server container $DOCKERID"
    docker stop "$DOCKERID" >/dev/null
else
    echo "no id of a running container server container found in '$DBDIR/dockerid.txt'. Nothing is stopped"
fi
DOCKERRM=$(docker rm ora-db-server 2>/dev/null)
case "$DOCKERRM" in
    '') echo "found no docker container with name 'ora-db-server'. Nothing removed" ;;
    * ) echo "removed docker container with name '$DOCKERRM'" ;;
esac
echo "starting docker image rbudde/openroberta_db:2.4.0 for the databases $*"
DOCKERID=$(docker run -d --name=ora-db-server -v $BASE/db:/opt/db -p 9001:9001 rbudde/openroberta_db:2.4.0 $*)
echo "database server container started with id $DOCKERID"
echo "$DOCKERID" >$DBDIR/dockerid.txt

echo "starting the database server finished"
