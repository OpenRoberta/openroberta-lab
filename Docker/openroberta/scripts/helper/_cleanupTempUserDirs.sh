#!/bin/bash

case "${HOURS}" in
  ''|'-') HOURS='12' ;;
  *)      : ;;
esac
echo "cleaning up temp user dirs older than ${HOURS} hours"
AGE="-mmin +$((${HOURS}*60))"

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}
TMP_DIR=${SERVER_DIR_OF_ONE_SERVER}/openrobertaTmp
isDirectoryValid ${TMP_DIR}
cd $TMP_DIR
WD=$(pwd)
if [[ "$WD" == $TMP_DIR ]]
then
    find . -ignore_readdir_race -maxdepth 1 ! -name '.' ! -name '..' $AGE -exec rm -rf -- {} \;
    RC=$?
else
    echo "cd $TMP_DIR did not succeed. This is dangerous! Analyse!"
    RC=12
fi
if [ ${RC} -ne 0 ]
then
  echo "cleaning up temp user dirs signals error by return code ${RC}"
else
  echo "successful cleaning up temp user dirs"
fi