#!/bin/bash

# shutdown the database. see the echo text below!

echo 'dbShutdown.sh [-log <path-to-log-file>] [db-uri]'
echo 'access the database and issue a "shutdown compact" command'
echo 'It is expected, that the lab runs in db server mode and not in embedded mode.'
echo 'If not, you have to supply the full uri of the database. Put the uri into single quotes to avoid globbing:'
echo '  - server mode (not needed, because this is the default): "jdbc:hsqldb:hsql://localhost/openroberta-db"'
echo "  - embedded mode: \"jdbc:hsqldb:file:./db-x.y.z/openroberta-db;ifexists=true\" for version x.y.z"

LOGFILE=./dbAdmin.log
case "$1" in
  -log) LOGFILE=$2
        shift; shift ;;
  *)	: ;;
esac

case "$1" in
  '') URI='jdbc:hsqldb:hsql://localhost/openroberta-db' ;;
  *)  URI="$1" ;;
esac

echo "shutdown the database at $URI and log into $LOGFILE"
java -cp lib/\* de.fhg.iais.roberta.main.Administration dbShutdown "$URI" >>$LOGFILE 2>&1