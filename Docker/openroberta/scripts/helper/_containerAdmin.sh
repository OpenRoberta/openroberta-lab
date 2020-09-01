#!/bin/bash

isServerNameValid ${SERVER_NAME}

CONTAINER="server-${SERVER_NAME}"
CMD="./admin.sh -q ${ADMIN_CMD}"

echo "executing in ${CONTAINER} the sh command ${CMD}"
docker exec ${CONTAINER} /bin/bash -c "${CMD}"
RC=$?
if [ ${RC} -ne 0 ]
then
  echo "admin signals error by return code ${RC}"
else
  echo "success in ${CONTAINER} for ${CMD}"
fi