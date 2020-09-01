# show the assembled activities from users once. This is the data delivered by the 2 REST calls /rest/alive and /rest/data/server/dbsessions
# Usage:
# SERVER_NAME: name of a server, as test, dev, dev1...dev9. REQUIRED.
# LOGFILE: the activity data is appended to this file. DEFAULT: ${BASE_DIR}/logs/server-activities-${SERVER_NAME}.log
# SERVERURL: the url where we get the http+db session data from. DEFAULT: retrieved via the server name
#
# if a variable has a default and its value is '-', the default is taken. Note, that trailing '-' can be omitted

isServerNameValid ${SERVER_NAME}

case "${LOGFILE}" in
  ''|'-') LOGFILE="${BASE_DIR}/logs/server-activities-${SERVER_NAME}.log" ;;
  *)      : ;;
esac
case "${SERVERURL}" in
  ''|'-') SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
          isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}
          cd ${SERVER_DIR_OF_ONE_SERVER}
          source ./decl.sh
          isDeclShValid
          SERVERURL="http://localhost:${PORT}" ;;
  *)      : ;;
esac

echo "${STAR_DATE_STAR}" >>${LOGFILE}
RESPONSE=$(curl ${SERVERURL}/rest/alive  2>/dev/null)
echo "${RESPONSE}" >> ${LOGFILE}
RESPONSE=$(curl ${SERVERURL}/rest/data/server/dbsessions 2>/dev/null)
echo "${RESPONSE}" >> ${LOGFILE}