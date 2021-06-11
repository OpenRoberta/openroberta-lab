#!/bin/bash

# dangerous parameters! Use not for production!
REMOTE_DEBUG=''
GC_LOGGING=''
while true
do
  case "$1" in
    remote.debug) REMOTE_DEBUG='-agentlib:jdwp=transport=dt_socket,address=0.0.0.0:2000,server=y,suspend=y'; shift ;;
    gc.logging)   GC_LOGGING="-XX:+PrintGC"; shift ;;
    *)            break ;;
  esac
done

DB_PARENTDIR=/opt/db
DB_NAME=openroberta-db
JAVA_LIB_DIR='lib'

CREATE_DB=no
if [[ ! -d ${DB_PARENTDIR} ]]; then
   CREATE_DB=yes
elif [[ ! -n "$(ls -A ${DB_PARENTDIR})" ]]; then
   CREATE_DB=yes
fi
if [[ ${CREATE_DB} = 'yes' ]]; then
   echo "No database found. Data base directory ${DB_PARENTDIR} does not exist or is empty. An empty database will be created."
   java -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME
fi

# more -d parameters are added by '$*' and can be supplied by the docker run command
java $REMOTE_DEBUG $GC_LOGGING -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.ServerStarter \
     -d database.mode=embedded \
     -d database.parentdir=$DB_PARENTDIR \
     -d database.name=$DB_NAME \
     -d server.staticresources.dir=staticResources \
     -d robot.crosscompiler.resourcebase='/opt/ora-cc-rsc' \
     $*
