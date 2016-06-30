#!/bin/bash

ip='0.0.0.0' # The ip default. For servers, 0.0.0.0 means "all IPv4 addresses on the local machine". (see: https://en.wikipedia.org/wiki/0.0.0.0)
port='1999'  # the port default.
lejosipaddr='10.0.1.1'                           # only needed for updating a lejos based ev3
oraversion='1.4.0-SNAPSHOT'                      # version for the export command (goes into openroberta.properties). BE CAREFUL !!!
databaseurl='jdbc:hsqldb:hsql://localhost/oradb' # server mode for the database. This setting should be used for production.
                                                 # embedded would be, e.g. jdbc:hsqldb:file:db/openroberta-db

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

function _helpFn {
  # be careful with file ora-help.txt . All words starting with "--" are extracted by compgen and considered COMMANDS used for completion
  echo 'THIS SCRIPT IS FOR DEVELOPERS. It is stored in directory '"$scriptdir"
  echo 'IF you execute ". '"$scriptdir"'/ora-please-source.sh", you get parameter completion when you type TAB (as usual ...)'
  echo ''
  $0 --java
  echo 'you may customize the script by editing it and defining new default values for properties. Actual values of the properties are:'
  echo 'oraversion='"$oraversion"
  echo 'port='"$port"
  echo 'databaseurl='"$databaseurl"
  echo 'lejosipaddr='"$lejosipaddr"' # only needed for updating a lejos based ev3'
  cat "$scriptdir/ora-help.txt"
}

function _startServerFn {
  _checkJava
  main='de.fhg.iais.roberta.main.ServerStarter'
  if [ -d OpenRobertaServer ]
  then
    echo "RUNNING OPEN ROBERTA LAB FROM A GIT REPOSITORY WITHOUT AN EXPLICIT EXPORT. THIS MAY BE DANGEROUS!"
    cd OpenRobertaServer
    run="java -cp target/resources/\* ${main} --port ${port}"
  else
    echo "RUNNING OPEN ROBERTA LAB FROM A INSTALLATION DIRECTORY (probably created by an --export command)"
    run="java -cp resources/\* ${main} --properties ${propfile} --ip ${ip} --port ${port}"
  fi
  echo "executing: $run"
  eval $run
}

function _startDatabaseFn {
  echo "directory in which the database resides (parameter 1) is $dbLocation"
  echo "database name (parameter 2) is $dbName"
  if [ -d OpenRobertaServer ]
  then
    echo "RUNNING THE DATABASE SERVER FROM A GIT REPOSITORY WITHOUT AN EXPLICIT EXPORT. THIS MAY BE DANGEROUS!"
    dir="OpenRobertaServer/target/resources"
  else
    echo "RUNNING OPEN ROBERTA LAB FROM A INSTALLATION DIRECTORY (probably created by an --export command)"
    dir="resources"
  fi
  run="java -cp $dir/hsqldb-2.3.2.jar org.hsqldb.Server --database.0 file:$dbLocation --dbname.0 $dbName --address ${ip} --port ${port}"
  run="java -cp $dir/hsqldb-2.3.2.jar org.hsqldb.Server --database.0 file:$dbLocation --dbname.0 $dbName"
  echo "executing: $run"
  eval $run
}

function _aliveFn {
  echo "checking for a server crash of server $serverurl every $every sec with $timeout sec timeout."
  if [[ "$mail" == 'no' ]]
  then
     echo "no mail will be sent if a crash is suspected"
  else
     echo "mail will be sent using the script \"$mail\" if a crash is suspected"
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
          echo ''
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
  which java > /dev/null
  if [[ $? != 0 ]]
  then
     checkJava=$'  java was NOT found on the PATH.\n'
  fi
  which javac > /dev/null
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
     echo "if you experience problems when trying to start the server, have a look at these hints:"
     echo "$checkJava"
  fi
  echo 'you are using the following java runtime:'
  echo $javaversion
}

function _exportApplication {
  if [[ "$dbOption" != '-e' &&  "$dbOption" != '-i' ]]
  then
    echo 'database option (first parameter) must be either -e or -i - exit 4'
    exit 4
  fi
  if [[ -e "$exportpath" ]]
  then
     echo "target directory \"$exportpath\" already exists - exit 4"
     exit 4
  fi
  echo "creating the target directory \"$exportpath\""
  mkdir "$exportpath"
  if [ $? -ne 0 ]
  then
    echo "creating the directory \"$exportpath\" failed - exit 4"
    exit 4
  fi
  if [[ "$dbOption" == '-e' ]]
  then
    echo "creating an empty data base"
    $0 --createemptydb "${exportpath}/db/openroberta-db"
  else
    echo "no database setup"
  fi
  echo "copying the web resources"
  webresources="OpenRobertaServer/staticResources"
  cp -r "$webresources" "$exportpath"
  echo 'creating directories for user programs and resources'
  mkdir "${exportpath}/userProjects"
  mkdir "${exportpath}/resources"
  mkdir "${exportpath}/crossCompilerResources"
  mkdir "${exportpath}/updateResources"
  echo "copying resources"
  cp OpenRobertaRuntime/crosscompiler-ev3-build.xml "${exportpath}"
  cp OpenRobertaServer/target/resources/*.jar "$exportpath/resources"
  cp OpenRobertaServer/target/updateResources/*.jar "$exportpath/updateResources"
  cp OpenRobertaServer/target/crossCompilerResources/*.jar "$exportpath/crossCompilerResources"
  cp ora.sh "$exportpath"
# -------------- begin of a here document -------------------------------------------------------------
  cat >"${exportpath}/openRoberta.properties" <<.eof
version = ${oraversion}
validversionrange.From = ${oraversion}
validversionrange.To = ${oraversion}
hibernate.connection.url = ${databaseurl}

crosscompiler.basedir = userProjects/
crosscompiler.build.xml = crosscompiler-ev3-build.xml
robot.updateResources.dir = updateResources
robot.crossCompilerResources.dir = crossCompilerResources
robot.type.list = ev3,oraSim
robot.type.default = ev3
.eof
# -------------- end of a here document ---------------------------------------------------------------
}

function _createemptydb {
  dbpath="$1"
  main='de.fhg.iais.roberta.main.Administration'
  run='java -cp "OpenRobertaServer/target/resources/*" '"${main} createemptydb ${dbpath}"
  echo "executing: $run"
  eval $run
}

function _updateLejos {
  run="scp -oKexAlgorithms=+diffie-hellman-group1-sha1 OpenRobertaServer/target/updateResources/EV3Menu.jar root@${lejosipaddr}:/home/root/lejos/bin/utils"
  echo "executing: ${run}"
  $run
  run="echo ${serverurl} | ssh root@${lejosipaddr} \"cat > /home/roberta/serverIP.txt\""
  echo "executing: ${run}"
  $run
  runtime="OpenRobertaServer/target/updateResources/EV3Runtime.jar"
  json='OpenRobertaServer/target/updateResources/json.jar'
  websocket='OpenRobertaServer/target/updateResources/Java-WebSocket.jar'
  run="ssh -oKexAlgorithms=+diffie-hellman-group1-sha1 root@${lejosipaddr} mkdir -p /home/roberta/lib"
  echo "executing: ${run}"
  $run
  run="scp -oKexAlgorithms=+diffie-hellman-group1-sha1 ${runtime} ${json} ${websocket} root@${lejosipaddr}:/home/roberta/lib"
  echo "executing: ${run}"
  $run
}

# ---------------------------------------- begin of the script ----------------------------------------------------
cmd="$1"; shift
if [[ "$cmd" == '' || "$cmd" == '--help' || "$cmd" == '-h' ]]
then
   _helpFn
   exit 0
elif [[ "$cmd" == '--java' ]]
then
   _checkJava
   exit 0
fi

continueReadingProperties='true'
while [[ "$continueReadingProperties" == 'true' ]]; do
   case "$cmd" in
      --lejosipaddr) lejosipaddr="$1"; shift;   cmd="$1"; shift ;;
      --ip)          ip="$1"; shift;            cmd="$1"; shift ;;
      --port)        port="$1"; shift;          cmd="$1"; shift ;;
      --version)     oraversion="$1"; shift;    cmd="$1"; shift ;;
      --databaseurl) databaseurl="$1"; shift;   cmd="$1"; shift ;;
      *)             continueReadingProperties='false'        ;;
   esac
done
if [[ "$cmd" == '' ]]
then
   exit 0
fi
case "$cmd" in
--reset-db)         git checkout HEAD -- OpenRobertaServer/db ;;
--sqlclient)        if [ -d OpenRobertaServer ]
                    then
                      echo "RUNNING THE SQL CLIENT FROM A GIT REPOSITORY WITHOUT AN EXPLICIT EXPORT. THIS MAY BE DANGEROUS!"
                      dir="OpenRobertaServer/target/resources"
                    else
                      echo "RUNNING THE SQL CLIENT FROM A INSTALLATION DIRECTORY (probably created by an --export command)"
                      dir="resources"
                    fi
                    java -jar $dir/hsqldb-2.3.2.jar --driver org.hsqldb.jdbc.JDBCDriver --url $databaseurl --user orA --password Pid ;;
--update-lejos)     serverurl="$1"
                    if [[ "$serverurl" == '' ]]
                    then
                      echo "the first parameter with the server address is missing. Exit 4"
                      exit 4
                    fi
                    _updateLejos ;;
--createemptydb)    dbpath="$1"
                    _createemptydb "$dbpath" ;;
--export)           if [ -d OpenRobertaServer ]
                    then
                      echo "Running the export from a git repository. This is ok!"
                    else
                      echo "NO GIT REPOSITORY FOUND. IMPOSSIBLE TO EXPORT (exit 4)!"
                      exit 4
                    fi 
                    dbOption=${1:--e}
                    exportpath="$2"
                    _exportApplication ;;
--start-server)     propfile="$1"
                    if [[ "$propfile" != '' ]] # property file given explicitly
                    then
                       propfile="file:$propfile"
                       echo "starting the server using supplied openroberta.properties from $propFile"
                    else
                       if [ -d OpenRobertaServer ]
                       then
                         echo "starting the server from a git repository. Using openroberta.properties from the classpath"
                       else
                         echo "starting the server from a installation directory (probably created by an --export command)"
                         echo "Using file \"openroberta.properties\" from the base directory"
                         propfile="file:openroberta.properties"
                       fi
                    fi
                    _startServerFn ;;
--start-db)         dbLocation="${1:-db/openroberta-db}"
                    dbName="${2:-oradb}"
                    _startDatabaseFn ;;
--alive)            serverurl="$1"
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
                    _aliveFn ;;
*)                  echo 'invalid command: "'"$cmd"'"'
                    exit 4 ;;
esac

exit 0
