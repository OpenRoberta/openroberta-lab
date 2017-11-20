#!/bin/bash

# start of the openroberta server and a separate database server
# typical use case: large servers with the need to access the database with a sql client when the server is running.
# E.g. the official server
# for parameter see: help message
# if the server runs version x.y.z, the the database is expected in directory x.y.z
# note: if the database version is lower than the server version, the server first updates the database
# - by creating a new directory,
# - copying the database content and
# - running the update script supplied with the server
# admin responsibilities:
# - avoid log files to grow and grow ...
# - remove old database directories after successful upgrade

DBLOGFILE='./ora-db.log'
SERVERLOGFILE='./ora-server.log'

echo 'start-ora-server-embedded.sh with the following optional parameters:'
echo "  -logdb <file-name>         database log file. Default: $DBLOGFILE"
echo "  -logserver <file-name>     server log file. Default: $SERVERLOGFILE"
echo '  -nohup                     start server with nohup. Default: without nohup'

NOHUP=''
while [ true ]
do
	case "$1" in
	  -logdb)     DBLOGFILE=$2
				  shift; shift ;;
	  -logserver) SERVERLOGFILE=$2
				  shift; shift ;;
	  -nohup)	  NOHUP="nohup" ;;
	  *)		  break ;;
	esac
done

echo 'have a look whether a database update is needed'
java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter -d database.parentdir=. -d database.mode=embedded --check-for-db-updates >>$SERVERLOGFILE 2>&1

echo 'start the database server and the openroberta server as separate processes'
serverVersion=$(java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter -v)
database=db-${serverVersion}/openroberta-db
echo "the database server will use database directory $database"
$NOHUP java -cp lib/\* org.hsqldb.Server --database.0 file:$database --dbname.0 openroberta-db \$* >>$DBLOGFILE 2>&1 &
sleep 5
$NOHUP java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter -d database.parentdir=. -d database.mode=server \$* >>$SERVERLOGFILE 2>&1 &