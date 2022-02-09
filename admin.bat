@ECHO OFF

SET ADMIN_LOG_FILE="admin/logs/admin.log"
SET SERVER_LOG_FILE="admin/logs/server.log"

SET DB_NAME="openroberta-db"
SET DB_PARENTDIR="db-embedded"
SET DB_URI_FILE="jdbc:hsqldb:file:%DB_PARENTDIR%/%DB_NAME%"

if not exist %DB_PARENTDIR% (
  java -cp "./lib/\*" de.fhg.iais.roberta.main.Administration "create-empty-db" %DB_URI_FILE% >>%ADMIN_LOG_FILE%
)

java -cp "./lib/\*" de.fhg.iais.roberta.main.ServerStarter "-d" "database.parentdir=%DB_PARENTDIR%" "-d" "database.name=%DB_NAME%" "-d" "server.staticresources.dir=./staticResources" "-d" "database.mode=embedded" "-d" "server.admin.dir=./admin" >>%SERVER_LOG_FILE%
