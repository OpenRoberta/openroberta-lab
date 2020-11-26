#!/bin/bash

function isDefined {
    local VAR="$1"; shift
    local VAL="${!VAR}"
    if [ "${VAL}" == '' ]
    then
        echo "variable ${VAR} is undefined or empty. Exit 12"
        exit 12
    fi
}

if [[ -f /proc/1/sched && $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container - exit 12 to avoid problems :-)'
   exit 12
fi

DB_MODE='embedded'
DB_NAME='openroberta-db'
DB_PARENTDIR='db-embedded'
JAVA_LIB_DIR='./lib'
ADMIN_DIR='./admin'
XMX=''
RDBG=''
QUIET='no'
 
while true
do
  case "$1" in
    -db-mode)      DB_MODE=$2
                   shift; shift ;;
    -db-name)      DB_NAME=$2
                   shift; shift ;;
    -db-parentdir) DB_PARENTDIR=$2
                   shift; shift ;;
    -java-lib-dir) JAVA_LIB_DIR=$2
                   shift; shift ;;
    -admin-dir)    ADMIN_DIR=$2
                   shift; shift ;;
    -Xmx*)         XMX=$1
                   shift ;;
    -rdbg)         RDBG='-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n'
                   shift ;;
    -q)            QUIET='yes'
                   shift ;;
    *)             break ;;
  esac
done

isDefined DB_MODE
isDefined DB_NAME
isDefined DB_PARENTDIR
isDefined JAVA_LIB_DIR
isDefined ADMIN_DIR

DB_URI_HSQL="jdbc:hsqldb:hsql://localhost/$DB_NAME"
DB_URI_FILE="jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME"
case "$DB_MODE" in
  embedded) DB_URI="$DB_URI_FILE" ;;
  server)   DB_URI="$DB_URI_HSQL" ;;
  *)        echo 'invalid db-mode. Exit 12'
            exit 12;;
esac

if [ "$QUIET" = 'no' ]
then
  cat admin-help.txt
fi

mkdir -p "$ADMIN_DIR/logs"
mkdir -p "$ADMIN_DIR/tutorial"
mkdir -p "$ADMIN_DIR/dbBackup"

ADMIN_LOG_FILE="$ADMIN_DIR/logs/admin.log"
SERVER_LOG_FILE="$ADMIN_DIR/logs/server.log"

# get the command
CMD="$1"; shift

# show the database URI for commands, which need them
case "$CMD" in
  version) : ;;
  start*)  : ;;
  *)       echo ''
           echo "operating in $DB_MODE data base mode"
           echo "the database URI in server mode   is: $DB_URI_HSQL"
           echo "the database URI in embedded mode is: $DB_URI_FILE"
           echo '' ;;
esac

# process the command
RC=0
case "$CMD" in
  sql-client)      echo 'command line sql client. Type commands, exit with an empty line'
                   java $RDBG $XMX  -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql-client "$DB_URI"
                   RC=$? ;;
  sql-gui)         hsqldbJar="$JAVA_LIB_DIR/hsqldb-2.4.0.jar"
                   java $RDBG $XMX -jar "${hsqldbJar}" --driver org.hsqldb.jdbc.JDBCDriver --url "$DB_URI" --user orA --password Pid
                   RC=$? ;;                   
  sql-exec)        SQL="$1";
                   echo "execute the sql statement '$SQL'"
                   java $RDBG $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql-exec "$DB_URI" "$SQL"
                   RC=$? ;;
  create-empty-db) java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db "$DB_URI_FILE" >>$ADMIN_LOG_FILE 2>&1
                   RC=$? ;;
  version)         java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version
                   RC=$? ;;
  '')              echo 'no command. Script terminates with exit 0'
                   exit 0 ;;
# ----------------------------------------------------------------------------------------------------------------------------------------------
  start-server)    case "$DB_MODE" in
                     server)   echo 'starting the jetty server with a data base in server mode. Server must be running' ;;
                     embedded) if [[ ! -d $DB_PARENTDIR ]]; then 
                                  echo 'No database found. An empty database will be created.'
                                  java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db "$DB_URI_FILE" >>$ADMIN_LOG_FILE 2>&1
                               fi
                               echo 'starting the jetty server with a data base in embedded mode' ;;
                   esac
                     
                   java $RDBG $XMX -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.ServerStarter \
                        -d server.staticresources.dir=./staticResources \
                        -d database.mode="$DB_MODE" \
                        -d database.parentdir=$DB_PARENTDIR \
                        -d database.name=$DB_NAME \
                        -d server.admin.dir=$ADMIN_DIR \
                        $* >>$SERVER_LOG_FILE 2>&1
                   ;;
# ----------------------------------------------------------------------------------------------------------------------------------------------
  *)                 echo "*** invalid command ignored: '$CMD' ***"
                     RC=0 ;;
esac

if [ $RC -ne 0 ]
then
  echo '*** the command did NOT succeed. Look into console output and logfiles ***'
fi
