#!/bin/bash
# start of the openroberta database server in a docker container.
# needs the list of databases as runtime arguments

function log { 
  echo $1
  echo $1 >>$DB_LOGFILE
}

case "$1" in
  -Xmx*) DATABASEXMX=$1; shift ;;
  *)     log 'first parameter must declare the maximum heap size (e.g. -Xmx4G). Exit 12'
	       exit 12 ;;
esac

case "$1" in
  '') log 'at least one parameter declaring a database is required. Exit 12'
	    exit 12 ;;
	*)  : ;;
esac

DB_BASEDIR=/opt/db
DB_ADMIN=/opt/dbAdmin
DB_LOGFILE="$DB_ADMIN/ora-db.log"

HSQL_DB_DECLS=''
I=0

for DB_NAME do
    case "$DB_NAME" in
    master)   : ;;
    test|dev) : ;;
    dev[1-9]) : ;;
	  *)        log "invalid name. Parameter must be 'test','dev','dev1'..'dev9', but is '$DB_NAME'. Exit 12"
	            exit 12 ;;
	esac
	HSQL_DB_DECLS="$HSQL_DB_DECLS --database.$I file:$DB_BASEDIR/$DB_NAME/openroberta-db --dbname.$I openroberta-db-$DB_NAME"
	let "I = $I + 1"
done

log 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'
log "the db server starting at $(date) will serve the databases $*"
log "db admin dir $DB_ADMIN stores logs and backups"
log 'mount db admin dir with -v on container start (often /data/openroberta/dbAdmin)'
log 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'

function trapSignals {
  log "signal caught. The database server will SHUTDOWN"
  kill -INT $child
}
trap trapSignals TERM INT
eval "java $DATABASEXMX -cp lib/\* org.hsqldb.Server $HSQL_DB_DECLS | tee -a $DB_LOGFILE &"
child="$!"
log "waiting for child with pid $child to terminate"
wait "$child"
