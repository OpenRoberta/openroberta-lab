#!/bin/bash

function _mkAndCheckDir {
  mkdir -p "$1"
  if [ $? -ne 0 ]
  then
    echo "creating the directory \"$1\" failed - exit 12"
    exit 12
  fi
}

function _aliveFn {
  case "$1" in
    '-q') quiet=result; shift ;;
    '-Q') quiet=down; shift ;;
    *)    quiet=false ;;
  esac
  serverUrl="$1"
  shift
  every="${1:-60}"
  timeout="${2:-30}"

  while :; do
    if [[ "$quiet" == 'false' ]]
    then
       curl --max-time $timeout "$serverUrl/rest/alive"
       rc=$?
       if [[ $rc == 0 ]]
       then
          echo "curl ok at `date`"
       fi
    else
       curl --max-time $timeout "$serverUrl/rest/alive" >/dev/null 2>&1
       rc=$?
    fi
    if [[ $rc != 0 ]]
    then
       echo "***** server seems to be down at `date` ******"
    elif [[ "$quiet" != 'down' ]]
    then
       echo "ok at `date`"
    fi
    sleep $every
  done
}

# check, whether java and javac are on the PATH. Check for the java version.
# variable checkJava contains debug information
function _checkJava {
  checkJava=''
  if [[ "$JAVA_HOME" == '' ]]
  then
     checkJava=${checkJava}$'JAVA_HOME is undefined.\n'
  fi
  which java 2>/dev/null 1>/dev/null
  if [[ $? != 0 ]]
  then
     checkJava=${checkJava}$'java was NOT found on the PATH.\n'
  fi
  which javac 2>/dev/null 1>/dev/null
  if [[ $? != 0 ]]
  then
     checkJava=${checkJava}$'javac was NOT found on the PATH.\n'
  fi
  javaversion=$(java -d64 -version 2>&1)
  case "$javaversion" in
    *not\ support*) checkJava=${checkJava}$'This may be a 32 bit java version. A 64 bit jdk is recommended.\n' ;;
    *)              : ;;
  esac
  javaversion=$(java -version 2>&1)
  case "$javaversion" in
    *1\.8\.*) : ;;
    *)        checkJava=${checkJava}$'This may be a java version less than 1.8.*. A version 8 is recommended.' ;;
  esac
  echo 'you are using the following java runtime:'
  echo "$javaversion"
  echo "$checkJava"
}

function _export {
  exportpath="$1"
  case "$2" in
    '')     gzip='n' ;;
    'gzip') gzip='y' ;;
    *)      echo "expected 'gzip' as 2nd param or nothing. Got: $2. Exit 12"
            exit 12 ;;
  esac
  if [ -e "$exportpath" ] && [ ! -z "$(ls -A $exportpath)" ]
  then
    echo "target directory \"$exportpath\" exists and is not empty - exit 12"
    exit 12
  fi
  
  echo "creating the target directory \"$exportpath\""
  _mkAndCheckDir "$exportpath"
  exportpath=$(cd "$exportpath"; pwd)
  serverVersion=$(java -cp OpenRobertaServer/target/resources/\* de.fhg.iais.roberta.main.Administration version)
  serverVersionForDb=$(java -cp OpenRobertaServer/target/resources/\* de.fhg.iais.roberta.main.Administration version-for-db)
  echo "server version: ${serverVersion} - db: ${serverVersionForDb}"
  
  echo "copying all jars"
  _mkAndCheckDir "${exportpath}/lib"
  cp OpenRobertaServer/target/resources/*.jar "$exportpath/lib"

  echo 'copying the staticResources'
  cp -r OpenRobertaServer/staticResources ${exportpath}/staticResources
  case "$gzip" in
    'n') echo 'staticResources are NOT gzip-ped. This increases load times when the internet connection is slow' ;;
    'y') numberGzFiles=$(find ${exportpath}/staticResources -type f | egrep '\.gz$' | wc -l)
         if [[ $numberGzFiles != 0 ]]
         then
           echo "\n$numberGzFiles gz-files found. This should NOT happen. Please CHECK staticResources in the Git repository\n"
         fi
         find ${exportpath}/staticResources -type f \
         | grep -Ev '\.(png|gif|mp3|gz|jpg|jpeg|wav|ogg)$' \
         | tr '\12' '\0' | tr '\a' '\0' \
         | xargs -n1 -0 gzip -9 -k -v -f
         ;;
  esac
  
  echo 'script for start of the server/db are copied'
  cp admin.sh ${exportpath}
  chmod ugo+rx admin.sh
  
  echo "NOTE: You are responsible to supply a usable database in directory db-${serverVersionForDb}"
}

# ---------------------------------------- begin of the script ----------------------------------------------------
if [ ! -d OpenRobertaServer ]
then
  echo 'please start this script from the root of the Git working tree - exit 12'
  exit 12
fi

JAVA_LIBS="OpenRobertaServer/target/resources" # done by mvn install ...

cmd="$1"
shift

case "$cmd" in
export) _export $* ;;

start-from-git) echo 'the script expects, that a mvn build was successful; if the start fails or the system is frozen, make sure that a database exists and NO *.lck file exists'
                echo '1. step: make an optional upgrade of the db 2. step: start the server'
                case "$1" in
                  '-REMOTE_DBG') REMOTE_DBG='-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y'
                                 shift ;;
                  *)             REMOTE_DBG='' ;;
                esac
                case "$1" in
                  '') CC_RESOURCE_DIR='.' ;;
                  *)  CC_RESOURCE_DIR="$1"
                      shift ;;
                esac
                java -cp ${JAVA_LIBS}/\* de.fhg.iais.roberta.main.Administration upgrade OpenRobertaServer
                RC=$?;
                if [ $RC != 0 ]
                then
                  echo 'database upgrade failed. Server NOT started. Exit 12'
                  exit 12
                fi
                java $RDBG -cp ${JAVA_LIBS}/\* de.fhg.iais.roberta.main.ServerStarter \
                     -d database.mode=embedded \
                     -d database.parentdir=OpenRobertaServer \
                     -d server.staticresources.dir=OpenRobertaServer/staticResources \
                     -d robot.crosscompiler.resourcebase="$CC_RESOURCE_DIR" \
                     $* ;;

''|help|-h)     cat ora-help.txt ;;

java)           _checkJava ;;

create-empty-db) serverVersionForDb="$1"
                if [[ "$serverVersionForDb" == '' ]]
                then
                  serverVersionForDb=$(java -cp ./${JAVA_LIBS}/\* de.fhg.iais.roberta.main.Administration version-for-db)
                fi
                databaseurl="jdbc:hsqldb:file:OpenRobertaServer/db-$serverVersionForDb/openroberta-db"
                echo -n "do you really want to create the db for version \"$serverVersionForDb\"? If it exists, it will NOT be damaged. 'y', 'n') "
                read ANSWER
                case "$ANSWER" in
                  y) : ;;
                  *) echo "nothing done. Exit 0"
                     exit 0 ;;
                esac
                echo "creating an empty db using the url $databaseurl"
                main='de.fhg.iais.roberta.main.Administration'
                java -cp ${JAVA_LIBS}/\* "${main}" create-empty-db "$databaseurl" ;;

check-xss)      serverVersionForDb="$1"
                if [[ "$serverVersionForDb" == '' ]]
                then
                  serverVersionForDb=$(java -cp ./${JAVA_LIBS}/\* de.fhg.iais.roberta.main.Administration version-for-db)
                fi
                databaseurl="jdbc:hsqldb:file:OpenRobertaServer/db-$serverVersionForDb/openroberta-db"
                main='de.fhg.iais.roberta.main.Administration'
                java -cp ${JAVA_LIBS}/\* "${main}" check-xss "$databaseurl" ;;
                  
renameRobot)    serverVersionForDb="$1"
                if [[ "$serverVersionForDb" == '' ]]
                then
                  serverVersionForDb=$(java -cp ./${JAVA_LIBS}/\* de.fhg.iais.roberta.main.Administration version-for-db)
                fi
                databaseurl="jdbc:hsqldb:file:OpenRobertaServer/db-$serverVersionForDb/openroberta-db"
                main='de.fhg.iais.roberta.main.Administration'
                java -cp ${JAVA_LIBS}/\* "${main}" rename "$databaseurl" $2 $3;;
                  
configurationCleanUp) serverVersionForDb="$1"
                if [[ "$serverVersionForDb" == '' ]]
                then
                  serverVersionForDb=$(java -cp ./${JAVA_LIBS}/\* de.fhg.iais.roberta.main.Administration version-for-db)
                fi
                databaseurl="jdbc:hsqldb:file:OpenRobertaServer/db-$serverVersionForDb/openroberta-db"
                main='de.fhg.iais.roberta.main.Administration'
                java -cp ${JAVA_LIBS}/\* "${main}" configuration-clean-up "$databaseurl" ;;

alive)          _aliveFn $* ;;

new-test-setup) base_dir="$1"
                if [[ -d $base_dir ]]
                then
                  echo "basedir '$base_dir' exists. Exit 12"
                  exit 12
                fi
                cp -r Docker/openroberta $base_dir
                cp Docker/_README.md $base_dir
                echo "new test server setup created in $base_dir. Edit 'config.sh' and setup db and servers now" ;;
new-server-in-test-setup)
                base_dir="$1"
                server_name="$2"
                if [[ ! -d $base_dir || ! -f $base_dir/config.sh ]]
                then
                  echo "basedir '$base_dir' no valid dir for test server setup. Exit 12"
                  exit 12
                fi
                server_dir="$base_dir/server/$server_name"
                db_dir="$base_dir/db/$server_name"
                if [[ -d "$server_dir" ||  -d "$db_dir" ]]
                then
                  echo "server dir or db dir for '$server_name' found. Exit 12"
                  exit 12
                fi
                cp -r Docker/openroberta/server/_server-template $server_dir
                cp -r Docker/openroberta/db/_empty-db-template $db_dir
                echo "new db and new server $server_name created in ${base_dir}. Edit '$base_dir/config.sh' and '$server_dir/decl.sh'" ;;
update-test-setup)
                base_dir="$1"
                if [[ ! -d $base_dir || ! -f $base_dir/config.sh ]]
                then
                  echo "basedir '$base_dir' no valid dir for test server setup. Exit 12"
                  exit 12
                fi
                rm -rf $base_dir/conf
                cp -r Docker/openroberta/conf $base_dir/conf
                cp Docker/_README.md $base_dir
                echo "configuration data copied to $base_dir/conf" ;;

*)              echo "invalid command: $cmd - exit 1"
                exit 1 ;;
esac

exit 0
