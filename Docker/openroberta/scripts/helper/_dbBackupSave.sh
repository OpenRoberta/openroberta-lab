#!/bin/bash

isDirectoryValid ${BASE_DIR}/${TO_PATH}
echo "backup is saved from directory ${FROM_PATH} to directory ${TO_PATH} on $(hostname) at $(date)"

SRCFILE_PREFIX="dbBackup-$(date +'%Y-%m-%d')-"
SRCFILE_PATTERN="${SRCFILE_PREFIX}"'*'
scp ${FROM_PATH}/${SRCFILE_PATTERN} ${BASE_DIR}/${TO_PATH}

(
  cd ${BASE_DIR}/${TO_PATH}
  set ${SRCFILE_PATTERN}
  for F do
    if [ "${F}" == "${SRCFILE_PATTERN}" ]
    then
      echo "backup FAILED. No backup available at ${FROM_PATH} for today?"
    else
      echo "a backup was saved to ${TO_PATH}/${F}"
    fi
  done
)