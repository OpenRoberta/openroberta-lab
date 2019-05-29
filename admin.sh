#!/bin/bash

# admin script

if [[ -f /proc/1/sched && $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container - exit 12 to avoid destruction and crash :-)'
   exit 12
fi

DB_URI='jdbc:hsqldb:hsql://localhost/openroberta-db'
JAVA_LIB_DIR='./lib'
ADMIN_DIR='./admin'
QUIET='no'
XMX=''
RDBG=''
 
while true
do
  case "$1" in
    -dbUri)        DB_URI=$2
                   shift; shift ;;
    -java-lib-dir) JAVA_LIB_DIR=$2
                   shift; shift ;;
    -admin-dir)    ADMIN_DIR=$2
                   shift; shift ;;
    -Xmx*)         XMX=$1
                   shift ;;
    -rdg)          RDBG='-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y'
                   shift ;;
    -q)            QUIET='yes'
                   shift ;;
    *)             break ;;
  esac
done

if [ "$QUIET" = 'no' ]
then
  echo 'THIS SCRIPT EXECUTES DANGEROUS COMMANDS. MISUSE MAY CRASH THE OPENROBERTA SERVER.'
  echo 'Execute this script from the base directory of an openroberta installation created by the "./ora.sh export" command'
  echo 'Parameter preceding the command:'
  echo '-dbUri <db-uri>     the database uri to use; default: jdbc:hsqldb:hsql://localhost/openroberta-db'
  echo '-java-lib-dir <dir> directory with the installation dir (containing the jars); default: ./lib'
  echo '-admin-dir <dir>    directory for log, database backups, ...; default: ./admin'
  echo '-Xmx                heap memory, e.g. -Xmx4G; default is the java default'
  echo '-rdbg               enable remote debug using port 8000; default: no remote debug'
  echo '-q                  quiet mode, default: verbose'
  echo ''
  echo 'admin.sh  [-dbUri <db-uri>] [-java-lib-dir <dir>] [-admin-dir <dir>] [-Xmx<size>] [-rdbg] [-q] <CMD>'
  echo ''
  echo 'Many commands expect a db URI. Usually this is for:'
  echo '  database server:   "jdbc:hsqldb:hsql://localhost/openroberta-db"'
  echo '  embedded database: "jdbc:hsqldb:file:{path-to-db-DIR}/openroberta-db"'
  echo ''
  echo '<CMD>s are:'
  echo '  backup [backup-dir]     access the database server and create a backup in directory [backup-dir]'
  echo '  shutdown                access the database and issue a "shutdown" command'
  echo '  sqlgui                  start a sql client with graphical user interface (GUI :-)'
  echo '  sqlclient               read SELECT commands from the terminal and execute them. Be careful, do NOT block the database'
  echo '  sqlexec <SQL>           execute a single <SQL> command'
  echo '  create-empty-db         create an empty database'
  echo '  upgrade [db-parent-dir] upgrade the database, if necessary. The database is accessed in embedded mode.'
  echo '                          Only for local server as RaspberryPI, NEVER prod! The version to upgrade to is read from the installation'
  echo '  version                 print the server version (may be suffixed with -SNAPSHOT) and terminate'
  echo '  version-for-db          print the database version (never contains -SNAPSHOT) and terminate'
  echo ''
  echo '  start-embedded-server   start the server in embedded mode. For small machines, on a RaspberryPI for instance.'
  echo '                          the admin (defaults to ./admin) dir needs subdirectories logs and tutorial'
  echo '                          Use -d robot.crosscompiler.resourcebase=... to supply crosscompiler resources (*.h, ...; get them from repo ora-cc-rsc)'
  echo '  start-2-server          start the database and the jetty server. DEPRECATED. Use DOCKER.'
  echo '                          the admin (defaults to ./admin) dir needs subdirectories logs and tutorial'
  echo '                          Use -d robot.crosscompiler.resourcebase=... to supply crosscompiler resources (*.h, ...; get them from repo ora-cc-rsc)'
  echo ''
  
  echo "the dir '$ADMIN_DIR' is used for various admin tasks as logging, database backups, tutorials"
fi

# needed for both start-* command. Expects a PID in variable 'child'
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
  version-for-db)        : ;;
  upgrade)               : ;;
  start-embedded-server) : ;;
  start-2-server)        : ;;
  *)                     echo ''
                         echo "the database URI is defined as: $DB_URI"
                         echo '' ;;
esac

# process the command
RC=0
case "$CMD" in
  'backup')          DB_BACKUP_DIR=$1; shift
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration db-backup $DB_URI $DB_BACKUP_DIR>>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;
  'shutdown')        echo "shutdown the database"
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration db-shutdown "$DB_URI" >>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;
  'sqlclient')       echo "command line sql client. Type commands, exit with an empty line"
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql-client "$DB_URI" ;;
  'sqlgui')          serverVersionForDb=$(java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version-for-db)
                     hsqldbJar="$JAVA_LIB_DIR/hsqldb-2.4.0.jar"
                     if [ -e ${hsqldbJar} ]
                     then
                       echo "using ${hsqldbJar} for starting the sql client with GUI"
                     else
                       echo "hsqldb not found - exit 12"
                       exit 12
                     fi
                     java -jar "${hsqldbJar}" --driver org.hsqldb.jdbc.JDBCDriver --url "$DB_URI" --user orA --password Pid ;;
  'sqlexec')         SQL="$1";
                     echo "execute the sql statement '$SQL'"
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql-exec "$DB_URI" "$SQL"
                     RC=$? ;;
  'create-empty-db') serverVersionForDb=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
                     database=db-${serverVersionForDb}/openroberta-db
                     java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:${database} >>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;
  'version')         java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version
                     RC=$? ;;
  'version-for-db')  java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version-for-db
                     RC=$? ;;
  'upgrade')         case "$1" in
                       '') echo 'database parent directory is missing - exit 12'
                           exit 12 ;;
                       *)  DB_PARENTDIR="$1"; shift ;;
                     esac
                     DB_VERSION=$(java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version-for-db)
                     java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration upgrade "$DB_PARENTDIR" >>$ADMIN_LOG_FILE 2>&1
                     RC=$? ;;

# ----------------------------------------------------------------------------------------------------------------------------------------
  'start-embedded-server')
      SERVER_LOG_FILE="$ADMIN_DIR/logs/embedded-server.log"

      serverVersionForDb=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
      database=db-${serverVersionForDb}/openroberta-db

      echo 'check whether any version of the database exists'
      shopt -s nullglob
      dbfiles=$(echo db-*)
      if [[ -z $dbfiles ]]; then 
          echo "A POTENTIAL PROBLEM: No databases found. An empty database for version ${serverVersionForDb} will be created."
          java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:${database} >>$ADMIN_LOG_FILE 2>&1
      fi

      echo 'check for database upgrade'
      java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade . >>$ADMIN_LOG_FILE 2>&1

      echo 'start the server with embedded database'
      trap propagateSignal SIGTERM SIGINT
      java $RDBG $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.ServerStarter \
           -d server.staticresources.dir=./staticResources \
           -d database.parentdir=. \
           -d database.mode=embedded \
           -d server.admin.dir=$ADMIN_DIR \
           $* >>$SERVER_LOG_FILE 2>&1 &
      child=$!
      wait "$child"
      ;;
# ----------------------------------------------------------------------------------------------------------------------------------------
  'start-2-server')
      echo 'DEPRECATED. Use docker container. Have a look at Docker/README.md'
      # exit 12
      
      DB_LOG_FILE="$ADMIN_DIR/logs/db.log"
      SERVER_LOG_FILE="$ADMIN_DIR/logs/server.log"
      serverVersionForDb=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
      database=db-${serverVersionForDb}/openroberta-db

      # MUST be called from an 'exported' dir (see 'ora.sh'). Dir '.' MUST be the db parent dir (i.e. contain 'db-* dirs)
      echo 'check whether any version of the database exists'
      shopt -s nullglob
      dbfiles=$(echo db-*)
      if [[ -z $dbfiles ]]; then 
          echo "A POTENTIAL PROBLEM: No databases found. An empty database for version ${serverVersionForDb} will be created."
          java -cp lib/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:${database} >>$ADMIN_LOG_FILE 2>&1
      fi

      # MUST be called from an 'exported' dir (see 'ora.sh'). Dir '.' MUST be the db parent dir (i.e. contain 'db-* dirs)
      echo 'check for database upgrade'
      java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade . >>$ADMIN_LOG_FILE 2>&1

      echo 'start the database server and the openroberta server as separate processes'
      echo "the database server will use database directory $database"
      java $XMX -cp lib/\* org.hsqldb.Server --database.0 file:$database --dbname.0 openroberta-db >>$DB_LOG_FILE 2>&1 &
      sleep 10 # time for the database to initialize
      java $RDBG $XMX -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
           -d server.staticresources.dir=./staticResources \
           -d database.parentdir=. \
           -d database.mode=server \
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
