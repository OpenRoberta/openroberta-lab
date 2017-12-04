#!/bin/bash

# shutdown the database. see the echo text below!

echo 'dbShutdown.sh [-log <path-to-log-file>]'
echo '  - access the database with URI defined below and issue a shutdown command'
echo 'It is expected, that the lab runs in db server mode and not in embedded mode.'
echo 'If not, the script fails without damaging something :-)'

URI='jdbc:hsqldb:hsql://localhost/openroberta-db'

LOGFILE=./dbAdmin.log
case "$1" in
  -log) LOGFILE=$2 ;;
  '')   ;;
  *)	echo "invalid parameter is IGNORED" ;;
esac

echo "logging into $LOGFILE"
java -cp lib/\* de.fhg.iais.roberta.main.Administration dbShutdown $URI >>$LOGFILE 2>&1