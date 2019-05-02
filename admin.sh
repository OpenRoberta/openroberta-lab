#!/bin/bash

# admin script

DB_URI='jdbc:hsqldb:hsql://localhost/openroberta-db'
JAVA_LIB_DIR='./lib'
ADMIN_DIR='./admin'
QUIET='no'
XMX=''
RDBG=''

while true
do
  case "$1" in
    -adminDir) ADMIN_DIR=$2
               shift; shift ;;
    -q)        QUIET='yes'
               shift ;;
    -Xmx*)     XMX=$1
               shift ;;
    -rdg)      RDBG='-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y'
               shift ;;
    -lib)      JAVA_LIB_DIR=$2
               shift; shift ;;
    -dbUri)    DB_URI=$2
               shift; shift ;;
    *)         break ;;
  esac
done

if [ "$QUIET" = 'no' ]
then
  echo 'THIS SCRIPT EXECUTES DANGEROUS COMMANDS. MISUSE MAY CRASH THE OPENROBERTA SERVER.'
  echo 'Execute this script from the base directory of an openroberta installation created by the "./ora.sh export" command'
  echo 'Defaults:'
  echo '-dbUri = jdbc:hsqldb:hsql://localhost/openroberta-db'
  echo '-java-lib-dir = ./lib'
  echo '-adminDir = ./admin'
  echo '-Xmx = '
  echo '-rdbg = '
  echo ''
  echo 'admin.sh [-q] [-adminDir <adminDir>] [-Xmx<size>] -rdbg [-lib <java-lib-dir>] [-dbUri <db-uri>] <CMD>'
  echo ''
  echo 'Many commands expect a db URI. The db-uri defaults to "jdbc:hsqldb:hsql://localhost/openroberta-db". But:'
  echo 'Often the db-name is a bit longer, as "openroberta-db-<DB-RESPECTIVE-SERVER-NAME>"'
  echo ''
  echo '<CMD>s are:'
  echo '  backup [backup-dir]     access the database server and create a backup in directory [backup-dir]'
  echo '                          If running in docker, the backup-dir is /opt/dbAdmin/dbBackup'
  echo '  shutdown                access the database and issue a "shutdown" command'
  echo '  sqlclient               read SELECT commands from the terminal and execute them. Be careful, do NOT block the database'
  echo '  sqlexec <SQL>           execute the well-quoted <SQL> command'
  echo '  upgrade [db-parent-dir] upgrade the database, if necessary. The database is accessed in embedded mode.'
  echo '                          Only for local server as RaspberryPI, NEVER prod! The version to upgrade to is read from the installation'
  echo '  version                 print the server version (may be suffixed with -SNAPSHOT) and terminate'
  echo '  version-for-db          print the database version (never contains -SNAPSHOT) and terminate'
  echo ''
  echo '  start-embedded-server   start the server in embedded mode. For small machines, on a RaspberryPI for instance.'
  echo '                          the admin (defaults to ./admin) dir needs subdirectories logs and tutorial'
  echo '                          Use -d robot.crosscompiler.resourcebase=... to supply crosscompiler resources (*.h, ...)'
  echo '                          -rdbg allows remote dbugging. Misuse will habe a dramatic impact on performance'
  echo '  start-2-server          start the database and the jetty server. DEPRECATED. Use DOCKER.'
  echo '                          the admin (defaults to ./admin) dir needs subdirectories logs and tutorial'
  echo '                          Use -d robot.crosscompiler.resourcebase=... to supply crosscompiler resources (*.h, ...)'
  echo '                          -rdbg allows remote dbugging. Misuse will habe a dramatic impact on performance'
  echo ''
  
  echo "logging/backup/tutorials via $ADMIN_DIR"
fi

# needed for both start-* command. Expects a PID in variable 'child'
function propagateSignal() { 
  echo "Caught signal. Propagate this to child process $child" 
  kill -TERM "$child" 2>/dev/null
}

# show the database URI for those commands, which need them
case "$CMD" in
  upgrade)               : ;;
  start-embedded-server) : ;;
  start-2-server)        : ;;
  *)                     echo ''
                         echo "the database URI is defined as: $DB_URI"
                         echo '' ;;
esac

ADMIN_LOG_FILE="$ADMIN_DIR/logs/dbAdmin.log"

CMD="$1"; shift
case "$CMD" in
  'backup')         DB_BACKUP_DIR=$1; shift
                    java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration dbBackup $DB_URI $DB_BACKUP_DIR>>$ADMIN_LOG_FILE 2>&1 ;;
  'shutdown')       echo "shutdown the database"
                    java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration dbShutdown "$DB_URI" >>$ADMIN_LOG_FILE 2>&1 ;;
  'sqlclient')      echo "command line sql client. Type commands, exit with an empty line"
                    java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sqlclient "$DB_URI" ;;
  'sqlGui')         serverVersionForDb=$(java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version-for-db)
                    hsqldbJar="$JAVA_LIB_DIR/hsqldb-2.4.0.jar"
                    if [ -e ${hsqldbJar} ]
                    then
                      echo "using ${hsqldbJar} for starting the sql client with GUI"
                    else
                      echo "hsqldb not found - exit 12"
                      exit 12
                    fi
                    java -jar "${hsqldbJar}" --driver org.hsqldb.jdbc.JDBCDriver --url "$DB_URI" --user orA --password Pid ;;
  'sqlexec')        SQL="$1";
                    echo "execute the sql statement '$SQL'"
                    java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration sql "$DB_URI" "$SQL" ;;
  'version')        java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version ;;
  'version-for-db') java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version-for-db ;;
  'upgrade')        case "$1" in
                      '') echo 'database parent directory is missing - exit 12'
                          exit 12 ;;
                      *)  DB_PARENTDIR="$1"; shift ;;
                    esac
                    DB_VERSION=$(java -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration version-for-db)
                    java $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.Administration upgrade "$DB_PARENTDIR" >>$ADMIN_LOG_FILE 2>&1 ;;

# ----------------------------------------------------------------------------------------------------------------------------------------
  'start-embedded-server')
      SERVERLOGFILE="$ADMIN_DIR/logs/embedded-server.log"

      serverVersionForDb=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
      database=db-${serverVersionForDb}/openroberta-db

      echo 'check whether any version of the database exists'
      shopt -s nullglob
      dbfiles=$(echo db-*)
      if [[ -z $dbfiles ]]; then 
          echo "A POTENTIAL PROBLEM: No databases found. An empty database for version ${serverVersionForDb} will be created."
          java -cp lib/\* de.fhg.iais.roberta.main.Administration createemptydb jdbc:hsqldb:file:${database}
      fi

      echo 'check for database upgrade'
      java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade . >>$SERVERLOGFILE 2>&1

      echo 'create admin dirs, if they do not exist'
      mkdir -p ./admin/logs
      mkdir -p ./admin/tutorial
      
      echo 'start the server with embedded database'
      trap propagateSignal SIGTERM SIGINT
      java $REMOTEDEBUG $XMX -cp $JAVA_LIB_DIR/\* de.fhg.iais.roberta.main.ServerStarter \
           -d server.staticresources.dir=./staticResources \
           -d database.parentdir=. \
           -d database.mode=embedded \
           -d server.admin.dir=$ADMIN_DIR \
           $* >>$SERVERLOGFILE 2>&1 &
      child=$!
      wait "$child"
      ;;
# ----------------------------------------------------------------------------------------------------------------------------------------
  'start-2-server')
      echo 'DEPRECATED. Use docker container. Have a look at Docker/README.md'
      # exit 12
      mkdir -p $ADMIN_DIR/logs
      DBLOGFILE="$ADMIN_DIR/logs/db.log"
      SERVERLOGFILE="$ADMIN_DIR/logs/server.log"
      ADMINLOGFILE="$ADMIN_DIR/logs/admin.log"
      serverVersionForDb=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
      database=db-${serverVersionForDb}/openroberta-db

      # MUST be called from an 'exported' dir (see 'ora.sh'). Dir '.' MUST be the db parent dir (i.e. contain 'db-* dirs)
      echo 'check whether any version of the database exists'
      shopt -s nullglob
      dbfiles=$(echo db-*)
      if [[ -z $dbfiles ]]; then 
          echo "A POTENTIAL PROBLEM: No databases found. An empty database for version ${serverVersionForDb} will be created."
          java -cp lib/\* de.fhg.iais.roberta.main.Administration createemptydb jdbc:hsqldb:file:${database} >>$ADMINLOGFILE 2>&1
      fi

      # MUST be called from an 'exported' dir (see 'ora.sh'). Dir '.' MUST be the db parent dir (i.e. contain 'db-* dirs)
      echo 'check for database upgrade'
      java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade . >>$ADMINLOGFILE 2>&1

      echo 'start the database server and the openroberta server as separate processes'
      echo "the database server will use database directory $database"
      java $XMX -cp lib/\* org.hsqldb.Server --database.0 file:$database --dbname.0 openroberta-db >>$DBLOGFILE 2>&1 &
      sleep 10 # time for the database to initialize
      java $REMOTEDEBUG $XMX -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
           -d server.staticresources.dir=./staticResources \
           -d database.parentdir=. \
           -d database.mode=server \
           -d server.admin.dir=$ADMIN_DIR \
           $* >>$SERVERLOGFILE 2>&1 &
      ;;
# ----------------------------------------------------------------------------------------------------------------------------------------
  '')               echo "no command. Script terminates" ;;
  *)                echo "invalid command. Ignored: \"$CMD\"" ;;
esac
