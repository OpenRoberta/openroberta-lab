#!/bin/bash

# start of the openroberta server with the database EMBEDDED. Optionally enable remote debugging.
# typical use case: small standalone servers, e.g. Raspberry PI. When the server is running, the database cannot
# be accessed by a sql client.
# if the server runs version x.y.z, the the database is expected in directory db-x.y.z
# note: if the database version is lower than the server version, first the database is upgraded
# admin responsibilities:
# - avoid log files to grow and grow ...
# - remove old database directories after successful upgrade

# DO NOT use this script with DOCKER, see the docker directory for alternatives

function propagateSignal() { 
  echo "Caught signal. Propagate this to child process $child" 
  kill -TERM "$child" 2>/dev/null
}

SERVERLOGFILE='./ora-server.log'
XMX=''
REMOTEDEBUG=''

remoteDebugDecl='-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y'

echo 'start-ora-server-with-db-embedded.sh with the following optional parameter:'
echo "  --logserver <file-name>     server log file. Default: $SERVERLOGFILE"
echo "  -Xmx<size>                  heap for the JVM, for example -Xmx2G. Default: nothing"
echo "  -rdbg                       enable remote debugging. Default: no remote debugging"
echo '  all parameters after these parameters are passed to the server'

while [ true ]
do
	case "$1" in
	  --logserver) SERVERLOGFILE=$2
                   shift; shift ;;
	  -Xmx*)       XMX=$1
	               shift ;;
	  -rdbg)       REMOTEDEBUG=$remoteDebugDecl
	               shift ;;
	  *)	       break ;;
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

echo 'start the server with embedded database'
trap propagateSignal SIGTERM SIGINT
java $REMOTEDEBUG $XMX -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
	 -d server.staticresources.dir=./staticResources -d database.parentdir=. -d database.mode=embedded $* >>$SERVERLOGFILE 2>&1 &
child=$!
wait "$child"
