#!/bin/bash

# backup the database. see the echo text below!

echo 'dbBackup.sh [-log <path-to-log-file>]'
echo '  - access the database with URI defined below and create a backup'
echo '  - The backup is written as tgz into the directory dbBackup.'
echo '  - The name of the backup contains the backup date formatted as yyyy-MM-dd-hh-mm-ss'

URI='jdbc:hsqldb:hsql://localhost/openroberta-db'

LOGFILE=./dbAdmin.log
case "$1" in
  -log) LOGFILE=$2 ;;
  '')   ;;
  *)	echo "invalid parameter is IGNORED" ;;
esac

echo "logging into $LOGFILE"
java -cp lib/\* de.fhg.iais.roberta.main.Administration dbBackup $URI >>$LOGFILE 2>&1