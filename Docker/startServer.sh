#!/bin/bash

# start of the openroberta server in a docker container. A -d parameter to the init script determines, whether the server
# runs in embedded or database server mode. NEVER ever set the -d parameter database.parentdir in a docker environment!

LOGFILE='./ora-server.log'
echo "startServer.sh [-log <file-name>] [server parameter ...]  Default log file name: $LOGFILE"

while [ true ]
do
	case "$1" in
	  -log) LOGFILE=$2
            shift; shift ;;
	  *)	break ;;
	esac
done

echo "Server log file is: $LOGFILE"
echo "Parameters for the server are: $*"

java  -cp lib/\* de.fhg.iais.roberta.main.ServerStarter $* >>$LOGFILE 2>&1