#!/bin/bash

TIMEOUT_CURL=50
SLEEP_BETWEEN_RESTARTS=3600

case "${SLEEP_BETWEEN_CHECKS}" in
  ''|'-') SLEEP_BETWEEN_CHECKS="60" ;;
  *)      : ;;
esac
case "${SLEEP_TO_AVOID_FALSE_POSITIVES}" in
  ''|'-') SLEEP_TO_AVOID_FALSE_POSITIVES="30" ;;
  *)      : ;;
esac

isServerNameValid ${SERVER_NAME}
echo "if '${SERVER_URL}' is not responding within ${TIMEOUT_CURL} seconds, then restart the container '${SERVER_NAME}'"
question 'do you really want such an potentially DANGEROUS automatic restart?'

function isServerAvailable { 
    START=$(($(date +%s%N)/1000000000))
    HTTPCODE=$(curl -o /dev/null -m ${TIMEOUT_CURL} -w '%{http_code}' -s ${SERVER_URL}/rest/alive)
    STOP=$(($(date +%s%N)/1000000000))
    ELAPSED=$((${STOP} - ${START}))
    if [ ${ELAPSED} -ge ${TIMEOUT_CURL} ] || [ ${HTTPCODE} != '200' ]
    then
        echo "false"
    else
        echo "true"
    fi
}

if [ $(isServerAvailable) == 'false' ]
then
    echo "the server is NOT available, when the auto-restart was activated - this is dangerous. Have you mistyped the url? Better to exit 12"
    exit 12
fi

(
  while [ 1 ]
  do
        if [ $(isServerAvailable) == 'false' ]
        then
            echo "$(date) container ${SERVER_NAME} did not respond within ${TIMEOUT_CURL} seconds, trying again" >>${BASE_DIR}/logs/autorestart.txt 2>&1
            sleep ${SLEEP_TO_AVOID_FALSE_POSITIVES}
            if [ $(isServerAvailable) == 'false' ]
            then
                echo "$(date) restart of container ${SERVER_NAME}, did not respond within ${TIMEOUT_CURL} sec when asked twice" >>${BASE_DIR}/logs/autorestart.txt 2>&1
                ${SCRIPT_MAIN}/run.sh -yes -q start ${SERVER_NAME} >>${BASE_DIR}/logs/autorestart.txt 2>&1
                sleep ${SLEEP_BETWEEN_RESTARTS}
            else
                echo "$(date) container ${SERVER_NAME} responded when asked the second time" >>${BASE_DIR}/logs/autorestart.txt 2>&1
            fi
        fi
        sleep ${SLEEP_BETWEEN_CHECKS}
  done
) &
PID=$!

(echo;echo "auto restart activated for server ${SERVER_NAME} at url ${SERVER_URL}";echo "it is checked every ${SLEEP_BETWEEN_CHECKS} sec whether the server is alive";\
 echo "if not answering, after ${SLEEP_TO_AVOID_FALSE_POSITIVES} sec the check is re-done to avoid false positives";\
 echo "the auto restart uses the pid ${PID}, which is disowned";echo) >>${BASE_DIR}/logs/autorestart.txt 2>&1
echo "the auto restart uses the pid ${PID}, which is disowned now"
disown ${PID}
