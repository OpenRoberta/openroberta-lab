# show the usage of some server resources:
# - file descriptors
# - threads
# - http sessions
# - db sessions
# Usage: with the following sh variables, which must be EXPORTed:
# SERVER_NAME: name of a server, as test, dev, dev1...dev9. Used to find both URL and PID. REQUIRED.
# LOGFILE: every 10 seconds the resource usage data is appended to this file. DEFAULT: ${BASE_DIR}/logs/server-resources.log
# LOWERLIMIT: if the number of open file descriptors is less than this value, nothing is logged. DEFAULT: 100
# UPPERLIMIT: if the number of open file descriptors is greater than this value, http sessions and db sessions are logged, too. DEFAULT: 200
# SERVERURL: the url where we get the http+db session data from. DEFAULT: retrieved via the server name
# DURATION: the number of hours after which the script should terminate itself. DEFAULT: 20
# PID: the pid of the server process. DEFAULT: retrieved via the server name
#
# if a variable has a default and its value is '-', the default is taken. For instance, this is a valid call which supplies a new upper limit
# and keeps the default everywhere else: .../run.sh show-resources test - - 150 - - -
# Note, that the last three '-' can be omitted

function showResourceLoop {
    headerMessage "start of showResources process with pid $$" >> ${LOGFILE}
    echo "observing server ${SERVER_NAME} lower ${LOWERLIMIT} upper ${UPPERLIMIT} url ${SERVERURL} pid ${PID}" >> ${LOGFILE}
    echo '' >> ${LOGFILE}
    
    SECOND_OF_START=$(date +%s)
    while [ true ]
    do
        sleep 10
        FD=$(ls /proc/${PID}/fd/ | wc -l)
        if [ "${FD}" -gt "${LOWERLIMIT}" ]
        then
            echo '.' >> ${LOGFILE}
            DATE=$(date +'%F %H:%M:%S')
            printf "%s fd: %-10d\n" "${DATE}" "${FD}" >> ${LOGFILE}
            pstree ${PID} >> ${LOGFILE}
            if [ "${FD}" -gt "${UPPERLIMIT}" ]
            then 
                RESPONSE=$(curl ${SERVERURL}/rest/data/server/resources  2>/dev/null)
                echo "${RESPONSE}" >> ${LOGFILE}
                RESPONSE=$(curl ${SERVERURL}/rest/data/server/dbsessions 2>/dev/null)
                echo "${RESPONSE}" >> ${LOGFILE}
                RESPONSE=$(curl ${SERVERURL}/rest/data/robot/summary     2>/dev/null)
                echo "${RESPONSE}" >> ${LOGFILE}
            fi
        fi
        SECOND_NOW=$(date +%s)
        DELTA=$(("${SECOND_NOW}" - "${SECOND_OF_START}"))
        if [ "${DELTA}" -gt "${DURATION}" ]
        then
            headerMessage "exit of show resources process $$" >> ${LOGFILE}
            exit 0
        fi
    done
}
export -f showResourceLoop

function guessPidOfServerRunningTheLab {
    PSTREE=$(pstree -alp | egrep '.*java.*PrintGC.*'"${SERVER_NAME}" | fgrep -v grep)
    [[ "${PSTREE}" =~ .*java,([0-9]*).* ]] && echo ${BASH_REMATCH[1]}
}

isServerNameValid ${SERVER_NAME}

case "${LOGFILE}" in
  ''|'-') LOGFILE="${BASE_DIR}/logs/server-resources-${SERVER_NAME}.log" ;;
  *)      : ;;
esac
case "${LOWERLIMIT}" in
  ''|'-') LOWERLIMIT=225 ;;
  *)      : ;;
esac
case "${UPPERLIMIT}" in
  ''|'-') UPPERLIMIT=250 ;;
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
case "${DURATION}" in
  ''|'-') DURATION=20 ;;
  *)      : ;;
esac
DURATION=$((${DURATION} * 60 * 60)) # convert hours to seconds
case "${PID}" in
  ''|'-') PID=$(guessPidOfServerRunningTheLab) ;;
  *)      : ;;
esac

echo "the log file of showResources is: ${LOGFILE}"
nohup bash -c showResourceLoop >/dev/null 2>&1 &