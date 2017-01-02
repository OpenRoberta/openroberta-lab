#!/bin/bash

lejosipaddr='10.0.1.1'                           # only needed for updating a lejos based ev3

databaseName=openroberta-db                      # the name of the database
databaseurlEmbedded=jdbc:hsqldb:file:db/${databaseName}
databaseurlServer=jdbc:hsqldb:hsql://localhost/${databaseName}

databaseurl=$databaseurlServer                   # HERE the database type is selected: either server or embedded

function mkAndCheckDir {
  mkdir "$1"
  if [ $? -ne 0 ]
  then
    echo "creating the directory \"$1\" failed - exit 4"
    exit 4
  fi
}

function _aliveFn {
  serverurl="$1"
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

  echo "checking for a server crash of server $serverurl every $every sec with $timeout sec timeout."
  if [[ "$mail" == 'no' ]]
  then
     echo "no mail will be sent if a crash is detected"
  else
     echo "mail will be sent using the script \"$mail\" if a crash is detected"
  fi
  while :; do
    if [[ "$quiet" == 'true' ]]
    then
       curl --max-time $timeout "http://$serverurl/rest/alive" > /dev/null
       rc=$?
    else
       curl --max-time $timeout "http://$serverurl/rest/alive"
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
     checkJava=${checkJava}$'  JAVA_HOME is undefined.\n'
  fi
  which java 2>/dev/null 1>/dev/null
  if [[ $? != 0 ]]
  then
     checkJava=${checkJava}$'  java was NOT found on the PATH.\n'
  fi
  which javac 2>/dev/null 1>/dev/null
  if [[ $? != 0 ]]
  then
     checkJava=${checkJava}$'  javac was NOT found on the PATH.\n'
  fi
  javaversion=`java -d64 -version 2>&1`
  case "$javaversion" in
    *not\ support*) checkJava=${checkJava}$'  This may be a 32 bit java version. A 64 bit jdk is recommended.\n' ;;
    *)              : ;;
  esac
  javaversion=`java -version 2>&1`
  case "$javaversion" in
    *1\.7\.*) : ;;
    *1\.8\.*) : ;;
    *)        checkJava=${checkJava}$'  This may be a java version less than 1.7.*. A version 7 is recommended.' ;;
  esac
  if [[ "$checkJava" != '' ]]
  then
     echo
     echo "if the server fails to start, have a look at these hints:"
     echo "$checkJava"
  fi
  echo 'you are using the following java runtime:'
  echo "$javaversion"
}

function _exportApplication {
  if [[ "$1" == '-createemptydb' ]]
  then
    dbOption=$1
    shift
  fi
  exportpath="$1"
  if [[ -e "$exportpath" ]]
  then
     echo "target directory \"$exportpath\" already exists - exit 4"
     exit 4
  fi
  echo "creating the target directory \"$exportpath\""
  mkAndCheckDir "$exportpath"
  if [[ "$dbOption" == '-createemptydb' ]]
  then
    echo "creating an empty data base"
    $0 --createemptydb "${exportpath}/db/${databaseName}"
  else
    echo; echo "YOU ARE RESPONSIBLE TO COPY THE DATABASE TO DIRECTORY db"; echo
  fi
  echo "copying all jars"
  mkAndCheckDir "${exportpath}/lib"
  cp OpenRobertaServer/target/resources/*.jar "$exportpath/lib"

  echo "copying resources for all robot plugins"
  set *
  for Robot do
    if [[ -d "$Robot" && -e "$Robot/resources" ]]
    then
      echo "  $Robot"
      mkAndCheckDir "${exportpath}/$Robot"
      cd $Robot
      cp -r --parents resources "${exportpath}/$Robot"
      cd ..
    fi
  done
# -------------- begin of here documents --------------------------------------------------------------
  cat >"${exportpath}/start-server.sh" <<.eof
java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter -d hibernate.connection.url=${databaseurl} \$*
.eof
  cat >"${exportpath}/start-db.sh" <<.eof
java -cp lib/hsqldb-2.3.2.jar org.hsqldb.Server --database.0 file:db/${databaseName} --dbname.0 $databaseName
.eof
# -------------- end of here documents ----------------------------------------------------------------
  chmod ugo+x "${exportpath}/start-server.sh" "${exportpath}/start-db.sh"
}

function _updateLejos {
  run="scp -oKexAlgorithms=+diffie-hellman-group1-sha1 RobotEV3/resources/updateResources/EV3Menu.jar root@${lejosipaddr}:/home/root/lejos/bin/utils"
  echo "executing: ${run}"
  $run
  run="echo ${serverurl} | ssh -oKexAlgorithms=+diffie-hellman-group1-sha1 root@${lejosipaddr} \"cat > /home/roberta/serverIP.txt\""
  echo "executing: ${run}"
  $run
  runtime="RobotEV3/resources/updateResources/EV3Runtime.jar"
  json='RobotEV3/resources/updateResources/json.jar'
  websocket='RobotEV3/resources/updateResources/Java-WebSocket.jar'
  run="ssh -oKexAlgorithms=+diffie-hellman-group1-sha1 root@${lejosipaddr} mkdir -p /home/roberta/lib"
  echo "executing: ${run}"
  $run
  run="scp -oKexAlgorithms=+diffie-hellman-group1-sha1 ${runtime} ${json} ${websocket} root@${lejosipaddr}:/home/roberta/lib"
  echo "executing: ${run}"
  $run
}

# ---------------------------------------- begin of the script ----------------------------------------------------
if [ -d OpenRobertaServer ]
then
  :
else
  echo "please start this script from the root of the Git working tree - exit 4"
  exit 4
fi
cmd="$1"
shift
case "$cmd" in
--export)         _exportApplication $* ;;
--start-from-git) $0 --reset-db
                  java -cp OpenRobertaServer/target/resources/\* de.fhg.iais.roberta.main.ServerStarter \
                       -d hibernate.connection.url=jdbc:hsqldb:file:OpenRobertaServer/db/${databaseName} \
                       $* ;;
--sqlclient)      dir="OpenRobertaServer/target/resources"
                  java -jar $dir/hsqldb-2.3.2.jar --driver org.hsqldb.jdbc.JDBCDriver --url $databaseurl --user orA --password Pid ;;

''|--help|-h)     # Be careful when editing the file 'ora-help.txt'. Words starting with "--" may be used by compgen for completion
                  $0 --java
                  echo ''
                  cat ora-help.txt ;;
--java)           _checkJava ;;

--update-lejos)   serverurl="$1"
                  if [[ "$serverurl" == '' ]]
                  then
                    echo "the first parameter with the server address is missing. Exit 4"
                    exit 4
                  fi
                  _updateLejos ;;
--reset-db)       rm -rf OpenRobertaServer/db
                  cp -a OpenRobertaServer/dbBase OpenRobertaServer/db ;;
--createemptydb)  dbpath="$1"
                  main='de.fhg.iais.roberta.main.Administration'
                  java -cp 'OpenRobertaServer/target/resources/*' "${main}" createemptydb "${dbpath}" ;;
--alive)          _aliveFn $* ;;
*)                echo "invalid command: $cmd"
                  exit 4 ;;
esac

exit 0
