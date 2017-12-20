#!/bin/bash
# start of the openroberta database server in a docker container.

function trapSignals {
	echo "signal caught. The database server will terminate with SHUTDOWN COMPACT"
	URI='jdbc:hsqldb:hsql://localhost/openroberta-db'
	java -cp lib/\* de.fhg.iais.roberta.main.Administration dbShutdown "$URI"
}

echo "usage: startDbServer.sh <database version as x.y.z>"

if [ -z "$VERSION" ]
then
    echo "db version is missing - exit 12"
    exit 12
fi
DB_BASEDIR=/opt/db

trap trapSignals SIGINT

DATABASE=db-${VERSION}/openroberta-db
echo "the database server will use database directory $DATABASE in base directory $DB_BASEDIR"
java -cp lib/\* org.hsqldb.Server --database.0 file:$DB_BASEDIR/$DATABASE --dbname.0 openroberta-db &
child="$!"
echo "waiting for child with pid $child to terminate"
wait "$child"
