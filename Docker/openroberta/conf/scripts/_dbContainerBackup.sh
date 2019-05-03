#!/bin/bash

isServerNameValid $DATABASE_NAME
if [ ! -d ${SERVER_DIR}/$DATABASE_NAME/export ]
then
    echo "server matching database $DATABASE_NAME has no export dir. Exit 12"
    exit 12
fi
JAVA_LIB_DIR=${SERVER_DIR}/$DATABASE_NAME/export/lib

DB_URI="jdbc:hsqldb:hsql://localhost/openroberta-db-$DATABASE_NAME"
DB_BACKUP_DIR=$DB_ADMIN_DIR/dbBackup
mkdir -p $DB_BACKUP_DIR

echo "starting the backup of database $DATABASE_NAME"
java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration dbBackup $DB_URI /opt/dbAdmin/dbBackup
echo "backup of database $DATABASE_NAME finished"
