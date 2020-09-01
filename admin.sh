#!/bin/bash

# admin script

if [[ -f /proc/1/sched && $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container - exit 12 to avoid problems :-)'
   exit 12
fi

DB_NAME='openroberta-db'
DB_PARENTDIR='db-server'
JAVA_LIB_DIR='./lib'
ADMIN_DIR='./admin'
QUIET='no'
XMX=''
RDBG=''
 
while true
do
  case "$1" in
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
    -rdbg|-rdg)    RDBG='-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y' # -rdg was a typo, kept for compatibility
                   shift ;;
    -q)            QUIET='yes'
                   shift ;;
    *)             break ;;
  esac
done

DB_URI="jdbc:hsqldb:hsql://localhost/$DB_NAME"

if [ "$QUIET" = 'no' ]
then
  echo 'THIS SCRIPT EXECUTES DANGEROUS COMMANDS. MISUSE MAY CRASH OPENROBERTA.'
  echo 'Execute this script from the <baseDirectory> of an openroberta installation created by the "./ora.sh export <baseDirectory> gzip" command'
  echo 'Parameter preceding the command:'
  echo '-db-name <db-name>  the database name; default: openroberta-db'
  echo '-db-parentdir <db-parentdir> the database parent directory; default: db-server. ONLY needed for start-server AND create-empty-db.'
  echo '-java-lib-dir <dir> directory with the installation dir (containing the jars); default: ./lib'
  echo '-admin-dir <dir>    directory for log, database backups, ...; default: ./admin'
  echo '-Xmx                heap memory, e.g. -Xmx4G; default is the java default'
  echo '-rdbg               enable remote debug using port 8000; default: no remote debug'
  echo '-q                  quiet mode, default: verbose'
  echo ''
  echo 'admin.sh  [-db-name <db-name>] [-java-lib-dir <dir>] [-admin-dir <dir>] [-Xmx<size>] [-rdbg] [-q] <CMD>'
  echo ''
  echo 'Many commands expect a db URI. Usually this is for:'
  echo '  database server:   "jdbc:hsqldb:hsql://localhost/<db-name>"'
  echo '  embedded database: "jdbc:hsqldb:file:<db-parentdir>/<db-name>"'
  echo ''
  echo '<CMD>s are:'
  echo '  backup [backup-dir]     access the database server and create a backup in directory [backup-dir]'
  echo '  shutdown                access the database and issue a "shutdown" command'
  echo '  sqlgui                  start a sql client with graphical user interface (GUI :-). Be careful, do NOT block the database'
  echo '  sqlguiEmbedded          start a sql client with graphical user interface (GUI :-). For the rare case, that a db is opened EMBEDDED'
  echo '  sqlclient               read SELECT commands from the terminal and execute them. Be careful, do NOT block the database'
  echo '  sqlexec <SQL>           execute a single <SQL> command'
  echo '  create-empty-db         create an empty database'
  echo '                          Only for local server as RaspberryPI, NEVER prod! The version to upgrade to is read from the installation'
  echo '  version                 print the server version (may be suffixed with -SNAPSHOT) and terminate'
  echo ''
  echo '  start-server            start the database and the jetty server.'
  echo '                          Use -d robot.crosscompiler.resourcebase=... to supply crosscompiler resources (*.h, ...; get them from repo ora-cc-rsc)'
  echo ''
  
  echo "the dir '$ADMIN_DIR' is used for various admin tasks as logging, database backups, tutorials"
fi

# needed for the start-server command. Expects a PID in variable 'child'
function propagateSignal() { 
  echo "Caught signal. Propagate this to child process $child" 
  kill -TERM "$child" 2>/dev/null
}

mkdir -p "$ADMIN_DIR/logs"
mkdir -p "$ADMIN_DIR/tutorial"
mkdir -p "$ADMIN_DIR/dbBackup"

ADMIN_LOG_FILE="$ADMIN_DIR/logs/admin.log"

# get the command
CMD="$1"; shift

# show the database URI for commands, which need them
case "$CMD" in
  version)               : ;;
  start-server)          : ;;
  *)                     echo ''
                         echo "the database URI is defined as: $DB_URI"
                         echo '' ;;
esac

# process the command
RC=0
case "$CMD" in
  'backup')          DB_BACKUP_DIR=$1; shift
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration db-backup $DB_URI $DB_BACKUP_DIR >>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;
  'shutdown')        echo 'shutdown the database'
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration db-shutdown "$DB_URI" >>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;
  'sqlclient')       echo 'command line sql client. Type commands, exit with an empty line'
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql-client "$DB_URI" ;;
  'sqlgui')          hsqldbJar="$JAVA_LIB_DIR/hsqldb-2.4.0.jar"
                     java $XMX -jar "${hsqldbJar}" --driver org.hsqldb.jdbc.JDBCDriver --url "$DB_URI" --user orA --password Pid ;;
  'sqlguiEmbedded')  hsqldbJar="$JAVA_LIB_DIR/hsqldb-2.4.0.jar"
                     databaseurl="jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME"
                     java $XMX -jar "${hsqldbJar}" --driver org.hsqldb.jdbc.JDBCDriver --url "$databaseurl" --user orA --password Pid ;;
  'sqlexec')         SQL="$1";
                     echo "execute the sql statement '$SQL'"
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql-exec "$DB_URI" "$SQL"
                     RC=$? ;;
  'create-empty-db') database="$DB_PARENTDIR"/"$DB_NAME"
                     java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:${database} >>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;
  'version')         java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version
                     RC=$? ;;

# ----------------------------------------------------------------------------------------------------------------------------------------
  'start-server')
      DB_LOG_FILE="$ADMIN_DIR/logs/db.log"
      SERVER_LOG_FILE="$ADMIN_DIR/logs/server.log"

      # MUST be called from an 'exported' dir (see 'ora.sh')
      echo 'check whether the database exists'
      if [[ ! -d $DB_PARENTDIR ]]; then 
          echo "A POTENTIAL PROBLEM: No databases found. An empty database will be created."
          java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME >>$ADMIN_LOG_FILE 2>&1
      fi

      echo 'start the database server and the openroberta server as separate processes'
      echo "the database server will use db parentdir $DB_PARENTDIR and db name $DB_NAME"
      java $XMX -cp lib/\* org.hsqldb.Server --database.0 file:$DB_PARENTDIR/$DB_NAME --dbname.0 $DB_NAME >>$DB_LOG_FILE 2>&1 &
      sleep 10 # time for the database to initialize
      java $RDBG $XMX -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
           -d server.staticresources.dir=./staticResources \
           -d database.mode=server \
           -d database.uri=localhost \
           -d database.name=$DB_NAME \
           -d server.admin.dir=$ADMIN_DIR \
           $* >>$SERVER_LOG_FILE 2>&1 &
      ;;
# ----------------------------------------------------------------------------------------------------------------------------------------
  '') echo "no command. Script terminates" ;;
  *)  echo "invalid command. Ignored: \"$CMD\""
      RC=$? ;;
esac

if [ $RC -ne 0 ]
then
  echo '*** the command did NOT succeed. Look into console output and logfiles ***'
fi
