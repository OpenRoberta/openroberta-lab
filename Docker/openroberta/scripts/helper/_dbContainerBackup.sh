#!/bin/bash

isServerNameValid ${DATABASE_NAME}

CONTAINER="server-${DATABASE_NAME}" # e.g. if you backup db "master", container "server-master" must be running, because the container triggers the sql backup command
DB_URI="jdbc:hsqldb:hsql://db-server/openroberta-db-${DATABASE_NAME}"
mkdir -p ${DB_ADMIN_DIR}/dbBackup/${DATABASE_NAME} # optional create of the backup dir for the database

echo "starting the backup of database ${DATABASE_NAME}"
if $(isWin)
then
    export MSYS_NO_PATHCONV=1
fi
docker exec ${CONTAINER} java -cp /opt/openroberta-lab/lib/\* de.fhg.iais.roberta.main.Administration db-backup ${DB_URI} /opt/dbAdmin/dbBackup/${DATABASE_NAME}
RC=$?
if [ ${RC} -eq 0 ]
then
  if [ $(getent group dbBackup) ]; then
    chmod g+r ${DB_ADMIN_DIR}/dbBackup/${DATABASE_NAME}/*.tgz
    chgrp dbBackup ${DB_ADMIN_DIR}/dbBackup/${DATABASE_NAME}/*.tgz
    echo "backup successful. Backup accessible by group 'dbBackup'"
  else
    echo "backup successful. This is a purely local backup. Group 'dbBackup' for external access of the backup not found"
  fi
else
  echo "backup ERROR. Got return code ${RC}"
fi