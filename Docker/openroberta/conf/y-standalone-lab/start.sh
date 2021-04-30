#!/bin/bash

DB_PARENTDIR=/opt/db
DB_NAME=openroberta-db
JAVA_LIB_DIR='lib'

CREATE_DB=no
if [[ ! -d ${DB_PARENTDIR} ]]; then
   CREATE_DB=yes
elif [[ -n "$(ls -A ${DB_PARENTDIR})" ]]; then
   CREATE_DB=yes
fi
if [[ ${CREATE_DB} = 'yes' ]]; then
   echo "No database found. Data base directory ${DB_PARENTDIR} does not exist or is empty. An empty database will be created."
   java -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME
fi
java $RDBG -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.ServerStarter \
     -d database.mode=embedded \
     -d database.parentdir=$DB_PARENTDIR \
     -d database.name=$DB_NAME \
     -d server.staticresources.dir=staticResources \
     -d robot.crosscompiler.resourcebase='/opt/ora-cc-rsc' \
     $*
