#!/bin/bash

isServerNameValid $DATABASE_NAME

CONTAINER="${INAME}-$DATABASE_NAME"
DB_URI="jdbc:hsqldb:hsql://${INAME}-db-server/openroberta-db-$DATABASE_NAME"
mkdir -p $DB_ADMIN_DIR/dbBackup/$DATABASE_NAME # optional create of the backup dir for the database

echo "starting the backup of database $DATABASE_NAME"
docker exec ${CONTAINER} java -cp /opt/openroberta-lab/lib/\* de.fhg.iais.roberta.main.Administration db-backup ${DB_URI} /opt/dbAdmin/dbBackup/$DATABASE_NAME
RC=$?
if [ $RC -ne 0 ]
then
  echo "backup signals error by return code $RC"
fi