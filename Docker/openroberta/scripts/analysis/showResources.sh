# show the usage of some server resources. Expected sh variables are:
# - file descriptors
# - threads
# - http sessions
# - db sessions
# Usage: 'showResources' with the following sh variables, which must be EXPORTed:
# SERVER_NAME: name of a server, as test, dev, dev1...dev9. Used to find both URL and PID
# LOWERLIMIT: if the number of open file descriptors is less than this value, nothing is logged. Default: 100
# UPPERLIMIT: if the number of open file descriptors is greater than this value, http sessions and db sessions are logged, too. Default: 200
# LOGFILE: every 10 seconds the resource usage data is appended to this file. Default: $BASE_DIR/logs/server-resources.log
# URL: the url where we get the http+db session data from. Default: retrieved via the server name
# PID: the pid of the server process. Default: retrieved via the server name

function showResourceLoop {
    echo 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx' >> $LOGFILE
    echo "showResources process has pid $$" >> $LOGFILE
    echo "server $SERVER_NAME lower $LOWERLIMIT upper $UPPERLIMIT url $SERVERURL pid $PID" >> $LOGFILE
    echo 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx' >> $LOGFILE
    while [ true ]
    do
        sleep 10
        FD=$(ls /proc/$PID/fd/ | wc -l)
        if [ "$FD" -gt "$LOWERLIMIT" ]
        then
            echo '.' >> $LOGFILE
            DATE=$(date +'%F %H:%M:%S')
            printf "%s fd: %-10d\n" "$DATE" "$FD" >> $LOGFILE
            pstree $PID >> $LOGFILE
            if [ "$FD" -gt "$UPPERLIMIT" ]
            then
                RESPONSE=$(curl $SERVERURL/rest/data/server/resources 2>/dev/null)
                echo "$RESPONSE" >> $LOGFILE
                RESPONSE=$(curl $SERVERURL/rest/data/robot/summary 2>/dev/null)
                echo "$RESPONSE" >> $LOGFILE
            fi
        fi
    done
}
typeset -fx showResourceLoop

function guessPidOfServerRunningTheLab {
    PSTREE=$(pstree -alp | egrep '.*java.*PrintGC.*'"$SERVER_NAME" | fgrep -v grep)
    [[ "$PSTREE" =~ .*java,([0-9]*).* ]] && echo ${BASH_REMATCH[1]}
}

case "$LOWERLIMIT" in
  '') LOWERLIMIT=100 ;;
  *)  : ;;
esac
case "$UPPERLIMIT" in
  '') UPPERLIMIT=200 ;;
  *)  : ;;
esac
case "$LOGFILE" in
  '') LOGFILE="$BASE_DIR/logs/server-resources.log" ;;
  *)  : ;;
esac
case "$SERVERURL" in
  '') isServerNameValid ${SERVER_NAME}
      SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
      isDirectoryValid $SERVER_DIR_OF_ONE_SERVER
      cd $SERVER_DIR_OF_ONE_SERVER
      source ./decl.sh
      isDeclShValid
      SERVERURL="http://localhost:$PORT" ;;
  *)  : ;;
esac
case "$PID" in
  '') PID=$(guessPidOfServerRunningTheLab) ;;
  *)  : ;;
esac

echo "the log file of showResources is: $LOGFILE"
nohup bash -c showResourceLoop >/dev/null 2>&1 &