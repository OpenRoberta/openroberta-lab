#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}

cd ${SERVER_DIR_OF_ONE_SERVER}
source ./decl.sh
isDeclShValid

ADMIN=${SERVER_DIR_OF_ONE_SERVER}/admin
ORA_CC_RSC=${BASE_DIR}/git/ora-cc-rsc
DB_SERVER=localhost:${DATABASE_SERVER_PORT}
EXPORT=${SERVER_DIR_OF_ONE_SERVER}/export
TMP_DIR=/tmp/openrobertaTmp

echo "this command assumes, that directory ${EXPORT} and git repo ${ORA_CC_RSC} are up-to-date"
echo 'this command assumes, that the server is not already running in a docker container and not used in AUTODEPLOY and auto-restart'
echo "this command assumes, that the group analysis exists and is allowed to get write permission to this installation and ${TMP_DIR}"
echo
echo "this command assumes, that YOU know, that garbage from ${TMP_DIR} has to be removed by YOU to avoid an out-of-space error on your devices"
question "this is a REALLY DANGEROUS COMMAND. Are you sure to deploy from the export directory of server ${SERVER_NAME}?"

sudo chgrp -R analysis ${BASE_DIR}
sudo chgrp -R analysis ${TMP_DIR}
sudo chmod -R g+rw ${BASE_DIR}
sudo chmod -R g+rw ${TMP_DIR}

cd ${EXPORT}

CMD="java -XX:+PrintGC -Xloggc:${ADMIN}/gcLog-$(date +%s).txt \
     -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
     -d database.mode=server \
     -d database.uri=${DB_SERVER} \
     -d database.name=openroberta-db-${SERVER_NAME} \
     -d server.log.configfile=${LOG_CONFIG_FILE} \
     -d server.log.level=${LOG_LEVEL} \
     -d server.staticresources.dir=./staticResources \
     -d plugin.tempdir=${TMP_DIR} \
     -d database.mode=server \
     -d robot.crosscompiler.resourcebase=${ORA_CC_RSC} \
     -d server.admin.dir=${ADMIN} \
     -d server.public=true \
     -d server.iptocountry.dir=${ADMIN}/iptocountrydb \
     -d server.port=${PORT}"
echo executing:
echo ${CMD}

eval ${CMD} &
PID=$!
sleep 20
disown ${PID}
MSG="started a server from directory ${EXPORT}, got pid ${PID} and disowned the process"
echo ${MSG} >> ${ADMIN}/startExport.txt
echo ${MSG}
