#!/bin/bash

# start of the openroberta server with the database EMBEDDED.
# typical use case: small standalone servers, e.g. Raspberry PI. When the server is running, the database cannot
# be accessed with a sql client.
# for parameter see: help message
# if the server runs version x.y.z, the the database is expected in directory db-x.y.z
# note: if the database version is lower than the server version, the server first updates the database
# - by creating a new directory,
# - copying the database content and
# - running the update script supplied with the server
# admin responsibilities:
# - avoid log files to grow and grow ...
# - remove old database directories after successful upgrade

SERVERLOGFILE='./ora-server.log'
START='plain'

echo 'start-ora-server-embedded.sh with the following optional parameters:'
echo "  -docker                    log to stdout, no nohup, no process spawning"
echo "  -logserver <file-name>     server log file. Default: $SERVERLOGFILE"
echo '  -nohup                     start server with nohup. Default: without nohup'

while [ true ]
do
	case "$1" in
	  -docker)    START='docker'
		      shift ;;
	  -logserver) SERVERLOGFILE=$2
                      shift; shift ;;
	  -nohup)     START="nohup"
		      shift ;;
	  *)	      break ;;
	esac
done

case "$START" in
docker) java  -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
	      -d database.parentdir=/opt/db -d database.mode=embedded \$* 2>&1 ;;
nohup)  nohup java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
	      -d database.parentdir=. -d database.mode=embedded \$* >>$SERVERLOGFILE 2>&1 & ;;
*)      java  -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
	      -d database.parentdir=. -d database.mode=embedded \$* >>$SERVERLOGFILE 2>&1 ;;
esac
