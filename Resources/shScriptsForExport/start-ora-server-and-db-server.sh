#!/bin/bash

# start of the openroberta server and a separate database server
# use case: a large server with the need to access the database by a sql client when the server is running.
# E.g. the official server
# for parameter see: help message
# if the server runs version x.y.z, the the database is expected in directory x.y.z
# note: if the database version is lower than the server version, the server first updates the database
# - by creating a new directory,
# - copying the database content and
# - running the update script supplied with the server
# admin responsibilities:
# - avoid log files to grow and grow ...
# - remove old database directories some time after successful upgrade

# DO NOT use this script with DOCKER, see the README in the docker directory

DBLOGFILE='./ora-db.log'
SERVERLOGFILE='./ora-server.log'
XMX=''

echo 'start-ora-server-and-db-server.sh with the following optional parameters:'
echo "  --logdb <file-name>     database log file. Default: $DBLOGFILE"
echo "  --logserver <file-name> server log file. Default: $SERVERLOGFILE"
echo "  -Xmx<size>              heap for the database server, for example -Xmx2G. Default: nothing"
echo '  all parameters after these parameters are passed to the server'

while [ true ]
do
	case "$1" in
	  --logdb)     DBLOGFILE=$2
		 		   shift; shift ;;
	  -Xmx*)       XMX=$1
	               shift ;;
	  --logserver) SERVERLOGFILE=$2
			 	   shift; shift ;;
	  *)		   break ;;
	esac
done

serverVersionForDb=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
database=db-${serverVersionForDb}/openroberta-db

echo 'check whether any version of the database exists'
shopt -s nullglob
dbfiles=$(echo db-*)
if [[ -z $dbfiles ]]; then 
    echo "A POTENTIAL PROBLEM: No databases found. An empty database for version ${serverVersionForDb} will be created."
    java -cp lib/\* de.fhg.iais.roberta.main.Administration createemptydb jdbc:hsqldb:file:${database}
fi

echo 'check for database upgrade'
java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade . >>$SERVERLOGFILE 2>&1

echo 'start the database server and the openroberta server as separate processes'
echo "the database server will use database directory $database"
java $XMX -cp lib/\* org.hsqldb.Server --database.0 file:$database --dbname.0 openroberta-db >>$DBLOGFILE 2>&1 &
sleep 10 # for the database to initialize
java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
     -d server.staticresources.dir=./staticResources -d database.parentdir=. -d database.mode=server $* >>$SERVERLOGFILE 2>&1 &