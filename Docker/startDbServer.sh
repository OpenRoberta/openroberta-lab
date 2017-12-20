#!/bin/bash

# start of the openroberta database server in a docker container.

SERVERLOGFILE='./ora-server.log'
echo "startServer.sh [-log <file-name>] [server parameter ...]  Default log file name: $SERVERLOGFILE"

while [ true ]
do
    case "$1" in
      -log) SERVERLOGFILE=$2
            shift; shift ;;
      *)    break ;;
    esac
done

echo "Server log file is: $SERVERLOGFILE"
echo "Parameters for the server are: $*"

echo java  -cp lib/\* de.fhg.iais.roberta.main.ServerStarter $* >>$SERVERLOGFILE 2>&1

VERSION='2.4.0'
LOGFILE='./ora-db.log'

echo "startDbServer.sh  [-version <x.y.z>] [-log <file-name>]  Default version: $VERSION, default log file name: $LOGFILE"

while [ true ]
do
	case "$1" in
	  -version) VERSION=$2
			    shift; shift ;;
	  -log)     LOGFILE=$2
			    shift; shift ;;
	  *)	    break ;;
	esac
done

DATABASE=db-${serverVersion}/openroberta-db
echo "the database server will use database directory $DATABASE"
java -cp lib/\* org.hsqldb.Server --database.0 file:$DATABASE --dbname.0 openroberta-db \$* >>$DBLOGFILE 2>&1