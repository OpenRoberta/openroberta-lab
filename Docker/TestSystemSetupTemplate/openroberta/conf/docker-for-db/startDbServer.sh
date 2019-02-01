#!/bin/bash
# start of the openroberta database server in a docker container.

function trapSignals {
	echo "signal caught. The database server will SHUTDOWN"
	URI='jdbc:hsqldb:hsql://localhost/openroberta-db'
	java -cp lib/\* de.fhg.iais.roberta.main.Administration dbShutdown "$URI"
}

case "$1" in
    '') echo 'at least one parameter declaring a database is required. Aborting'
	    exit 12 ;;
	*)  : ;;
esac

DB_BASEDIR=/opt/db
echo "usage: startDbServer.sh <name-for-db-uri-1>...<name-for-db-uri-n>"
echo "the database server will use base directory $DB_BASEDIR"

trap trapSignals SIGINT

echo "the database server will use base directory $DB_BASEDIR"

PARMS=''
I=0

for PARM do
    case "$PARM" in
    test|dev|dev[1-9]) : ;;
	*                ) echo "invalid name. Parameter must be 'test','dev','dev1'..'dev9', but is '$PARM'. Aborting"
	                   exit 12;;
	esac
	PARMS="$PARMS --database.$I file:$DB_BASEDIR/$PARM/openroberta-db --dbname.$I openroberta-db-$PARM"
	let "I = $I + 1"
done

eval "java -Xmx4G -cp lib/\* org.hsqldb.Server $PARMS &"
child="$!"
echo "waiting for child with pid $child to terminate"
wait "$child"
