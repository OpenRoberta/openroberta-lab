#!/bin/bash

DBDIR=$BASE/db
isDirectoryValid $DBDIR

echo "stopping the database container 'ora-db-server'"
docker stop 'ora-db-server' >/dev/null
DOCKERRM=$(docker rm ora-db-server 2>/dev/null)
case "$DOCKERRM" in
    '') echo "found no container 'ora-db-server'. Nothing removed" ;;
    * ) echo "removed container '$DOCKERRM'" ;;
esac
