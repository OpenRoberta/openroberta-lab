#!/bin/bash

function mkAndCheckDir {
  if [ -e "$1" ]
  then
    return
  fi
  mkdir "$1"
  if [ $? -ne 0 ]
  then
    echo "creating the directory \"$1\" failed - exit 1"
    exit 1
  fi
}

function _aliveFn {
  serverUrl="$1"
  shift
  if [[ "$1" == '-q' ]]
  then
    quiet='true'
    shift
  else
    quiet='false'
  fi
  every="${1:-60}"
  timeout="${2:-30}"
  mail="${3:-no}"

  echo "checking for a server crash of server $serverUrl every $every sec with $timeout sec timeout."
  if [[ "$mail" == 'no' ]]
  then
     echo "no mail will be sent if a crash is detected"
  else
     echo "mail will be sent using the script \"$mail\" if a crash is detected"
  fi
  while :; do
    if [[ "$quiet" == 'true' ]]
    then
       curl --max-time $timeout "http://$serverUrl/rest/alive" > /dev/null
       rc=$?
    else
       curl --max-time $timeout "http://$serverUrl/rest/alive"
       rc=$?
       if [[ $rc == 0 ]]
       then
          echo "ok `date`"
       fi
    fi
    if [[ $rc != 0 ]]
    then
       echo "************************ server seems to be down `date` ************************"
       if [[ "$mail" != 'no' ]]
       then
          sh $mail
       fi
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

function _exportApplication {
    exportpath="$1"
    if [ -e "$exportpath" ] && [ ! -z "$(ls -A $exportpath)" ]
    then
        echo "target directory \"$exportpath\" exists and is not empty - exit 1"
        exit 1
    else
        :
    fi
    mkAndCheckDir "$exportpath"
    exportpath=$(cd "$exportpath"; pwd)
    cd OpenRobertaParent
	  serverVersion=$(java -cp OpenRobertaServer/target/resources/\* de.fhg.iais.roberta.main.ServerStarter -v)
    echo "serverVersion: ${serverVersion}"
    echo "created the target directory \"$exportpath\""
    echo "copying all jars"
    mkAndCheckDir "${exportpath}/lib"
    cp OpenRobertaServer/target/resources/*.jar "$exportpath/lib"
    mkAndCheckDir "${exportpath}/dbBase"
    cp OpenRobertaServer/dbBase/* "$exportpath/dbBase"

    echo 'copying resources for all robot plugins'
    set *
    mkAndCheckDir "${exportpath}/OpenRobertaParent"
    for Robot do
        if [[ -d "$Robot" && -e "$Robot/resources" ]]
        then
            echo "  $Robot"
            mkAndCheckDir "${exportpath}/OpenRobertaParent/$Robot"
            cd "$Robot"
            cp -r --parents resources "${exportpath}/OpenRobertaParent/$Robot"
            cd ..
        fi
    done
    cd ..
    echo 'copy scripts'
    cp start-*.sh dbBackup.sh dbShutdown.sh ${exportpath}
    chmod ugo+rx ${exportpath}/*.sh
	echo "You are responsible to supply a usable database in directory db-${serverVersion}"
}

function _updateLejos {
  cd OpenRobertaParent
  run="scp -oKexAlgorithms=+diffie-hellman-group1-sha1 RobotEV3/resources/updateResources/lejos_${lejosVersion}/EV3Menu.jar root@${LEJOSIPADDR}:/home/root/lejos/bin/utils"
  echo "executing: ${run}"
  $run
  run="echo ${serverUrl} | ssh -oKexAlgorithms=+diffie-hellman-group1-sha1 root@${LEJOSIPADDR} \"cat > /home/roberta/serverIP.txt\""
  echo "executing: ${run}"
  $run
  runtime="RobotEV3/resources/updateResources/lejos_${lejosVersion}/EV3Runtime.jar"
  json="RobotEV3/resources/updateResources/lejos_${lejosVersion}/json.jar"
  websocket="RobotEV3/resources/updateResources/lejos_${lejosVersion}/Java-WebSocket.jar"
  run="ssh -oKexAlgorithms=+diffie-hellman-group1-sha1 root@${LEJOSIPADDR} mkdir -p /home/roberta/lib"
  echo "executing: ${run}"
  $run
  run="scp -oKexAlgorithms=+diffie-hellman-group1-sha1 ${runtime} ${json} ${websocket} root@${LEJOSIPADDR}:/home/roberta/lib"
  echo "executing: ${run}"
  $run
  cd ..
}

# ---------------------------------------- begin of the script ----------------------------------------------------
if [ -d OpenRobertaParent ]
then
  :
else
  echo 'please start this script from the root of the Git working tree - exit 1'
  exit 1
fi
cmd="$1"
shift

# often used values
DBEMPTY='OpenRobertaParent/OpenRobertaServer/dbEmpty'
DBBASE='OpenRobertaParent/OpenRobertaServer/dbBase'

case "$cmd" in
--export)         _exportApplication $* ;;

--start-from-git) java -cp OpenRobertaParent/OpenRobertaServer/target/resources/\* de.fhg.iais.roberta.main.ServerStarter -d database.mode=embedded -d database.parentdir=OpenRobertaParent/OpenRobertaServer $* ;;

--sqlclient)      dir="OpenRobertaParent/OpenRobertaServer/target/resources"
                  databaseurl="$1"
                  java -jar "$dir/hsqldb-*.jar" --driver org.hsqldb.jdbc.JDBCDriver --url "$databaseurl" --user orA --password Pid ;;

''|--help|-h)     # Be careful when editing the file 'ora-help.txt'. Words starting with "--" are used by compgen for completion
                  $0 --java
                  cat ora-help.txt ;;

--java)           _checkJava ;;

--update-lejos)   serverUrl="$1"
                  lejosVersion="$2"
				  LEJOSIPADDR='10.0.1.1'
                  if [[ "$serverUrl" == '' ]]
                  then
                    echo "the server URL is missing. Exit 1"
                    exit 1
                  elif [[ "$lejosVersion" == '' ]]
                  then
                    echo "the lejos version (0 or 1) is missing missing. Exit 1"
                    exit 1
                  fi
                  _updateLejos ;;

--reset-db)       echo -n "do you really want to reset the ACTUAL database to $DBBASE? The old content will be LOST. 'yes', 'no') "
                  read ANSWER
                  case "$ANSWER" in
                  yes) : ;;
                  *)   echo "nothing done"
                       exit 0 ;;
                  esac
				  serverVersion=$(java -cp OpenRobertaParent/OpenRobertaServer/target/resources/\* de.fhg.iais.roberta.main.ServerStarter -v)
                  DB="OpenRobertaParent/OpenRobertaServer/db-${serverVersion}"
				  echo "the database at $DB is resetted to $DBBASE"
				  rm -rf "$DB"
                  cp -a "$DBBASE" "$DB" ;;

--createEmptydb)  echo -n "do you really want to create $DBEMPTY? The old empty db will be LOST. 'yes', 'no') "
                  read ANSWER
                  case "$ANSWER" in
                  yes) : ;;
                  *)   echo "nothing done"
                       exit 0 ;;
                  esac
                  rm -rf $DBEMPTY
				  main='de.fhg.iais.roberta.main.Administration'
                  java -cp 'OpenRobertaParent/OpenRobertaServer/target/resources/*' "${main}" createemptydb "$DBEMPTY/openroberta-db" ;;

--resetDbBase)    echo -n "do you really want to make DBBASE equal to DBEMPTY? The old defaults will be LOST. 'yes', 'no') "
                  read ANSWER
                  case "$ANSWER" in
                  yes) : ;;
                  *)   echo "nothing done"
                       exit 0 ;;
                  esac
				  rm -rf $DBBASE
				  cp -r $DBEMPTY $DBBASE ;;

--alive)          _aliveFn $* ;;

*)                echo "invalid command: $cmd - exit 1"
                  exit 1 ;;
esac

exit 0
