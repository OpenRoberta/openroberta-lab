#!/bin/bash
# start of the openroberta database server in a docker container.
# needs the list of databases to serve as runtime arguments

function trapSignals {
	echo "signal caught. The database server will SHUTDOWN"
	URI='jdbc:hsqldb:hsql://localhost/openroberta-db'
	java -cp lib/\* de.fhg.iais.roberta.main.Administration dbShutdown "$URI"
}

function log { 
  echo $1
  echo $1 >>$DB_LOGFILE
}

case "$1" in
    '') echo 'at least one parameter declaring a database is required. Exit 12'
	    exit 12 ;;
	*)  : ;;
esac

DB_BASEDIR=/opt/db
DB_BACKUPDIR=/opt/administration/dbBackup
DB_LOGFILE=/opt/administration/logs/ora-db.log
trap trapSignals SIGINT

PARMS=''
I=0

for PARM do
    case "$PARM" in
    master)   : ;;
    test|dev) : ;;
    dev[1-9]) : ;;
	  *)        echo "invalid name. Parameter must be 'test','dev','dev1'..'dev9', but is '$PARM'. Aborting"
	            exit 12 ;;
	esac
	PARMS="$PARMS --database.$I file:$DB_BASEDIR/$PARM/openroberta-db --dbname.$I openroberta-db-$PARM"
	let "I = $I + 1"
done

log "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
log "the database server will use base directory $DB_BASEDIR"
log "and serve the databases $*"
log "database backups with openroberta-db-<DB-RESP-SERVER-NAME>"
log "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

eval "java -Xmx4G -cp lib/\* org.hsqldb.Server $PARMS >>$DB_LOGFILE &"
child="$!"
echo "waiting for child with pid $child to terminate"
wait "$child"
