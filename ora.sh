#!/bin/bash

serverurl='10.0.1.10:1999'
ev3ipaddr='10.0.1.1'
oraversion='1.3.2-SNAPSHOT'

function _helpFn {
  # be careful when changing the help function.
  # All words starting with "--" are extracted by a compgen function and considered COMMANDS!
  echo 'THIS SCRIPT IS FOR DEVELOPERS';
  echo 'IF you source like ". ora-please-source.sh" before using this script, you get completion when you type TAB (as usual ...)';
  echo '';
  $0 --java
  echo 'you may customize the script by defining default values for properties. Actual values of the properties are:';
  echo 'ev3ipaddr='"$ev3ipaddr";
  echo 'oraversion='"$oraversion";
  echo 'serverurl='"$serverurl";
  echo '';
  echo '1.set properties for LATER use in the SAME call';
  echo '';
  echo '  --ev3ipaddr {IP-ADDR}              set the ip addr of the EV3 brick for further commands';
  echo '  --version {VERSION}                set the version to something like d.d.d where d is one or more digits, e.g. 1.2.0'
  echo '                                     only used when an installation is exported. Use with care.'
  echo '  --serverurl {IP:PORT}              set a default url (ip address plus port) to which the server is associated.'
  echo '                                     useful for the commands --start --alive --setev3serverinfo'
  echo '';
  echo '2.debugging commands';
  echo '';
  echo '  --help                             get help';
  echo '  --java                             check whether java and javac are on the path and the JDK versin matches. Show the java version';
  echo ''
  echo '  --checkout-db OR --restore-db      restore the state of the database to the state last checked out.'
  echo '                                     this makes sense, if you changed the db during test and dont want to commit your changes'
  echo '  --alive {-q} {EVERY} {TIMEOUT} {MAIL} check after EVERY sec (default: 60) if the server is alive.'
  echo '                                     The server is assumed to have crashed, if it does not answer within TIMEOUT sec (default: 10).'
  echo '                                     If the server is assumed to have crashed, send a mail by calling the script MAIL (default: NO)'
  echo '                                     -q is the quiet mode: report crashes only'
  echo '                                     a usefull call, reporting to stdout, is e.g. ora.sh --serverurl localhost:1999 --alive 10 10'
  echo '  --sqlclient                        start the hsqldb client to query the database. The openroberta server must NOT run and'
  echo '                                     and thus access the database. Note, that hsqldb is not used in db server node, but standalone.';
  echo '';
  echo '3.commands available after a SUCCESSFUL mvn {clean} install';
  echo '';
  echo '  --start {PROPERTY-FILE}            start of the server, optionally supply a property file (default: openRoberta.properties';
  echo '                                     to be used after an --export of the application.' ;
  echo '  --scpev3menu                       scp the ev3menu.jar to the EV3, uses ev3ipaddr';
  echo '                                     the root password is "", thus hit return if you are asked';
  echo '  --scpev3libs                       scp libraries to the EV3, uses ev3ipaddr';
  echo '                                     the root password is "", thus hit return everytime you are asked';
  echo '  --setev3serverinfo                 Create a file with the ip address and port of the server.'
  echo '                                     the root password is "", thus hit return everytime you are asked';
  echo '  --createemptydb DB-NAME            create an empty database with all tables needed for the OpenRoberta server'
  echo '                                     Needs a data base name as a prefix for the files/directoriy to be created.'
  echo '                                     if the database exists, it is not recreated. If a table "PROGRAM" is found'
  echo '                                     in an existing database, it is assumed, that the setup has already been done.'
  echo '  --export [-e|-i] INSTALLATION-DIR  create a self-contained installation with an empty database (-e) or without database setup (-i)'
  echo '                                     with -i the caller is responsible to supply a usable database.'
}

function _startFn {
  IFS=':' read -ra IpPort <<< "$serverurl"
  Ip="${IpPort[0]}"
  Port="${IpPort[1]}"
  _checkJava;
  main='de.fhg.iais.roberta.main.ServerStarter'
  run="java -cp resources/\* ${main} --properties ${propfile} --ip ${Ip} --port ${Port}"
  echo "executing: $run"
  eval $run
}

function _aliveFn {
  echo "checking for a server crash every $every sec with $timeout sec timeout."
  if [[ "$mail" == 'no' ]]; then
     echo "no mail will be sent if a crash is suspected"
  else
     echo "mail will be sent using the script \"$mail\" if a crash is suspected"
  fi
  while :; do
    if [[ "$quiet" == 'true' ]]; then
       curl --max-time $timeout "http://$serverurl/alive" > /dev/null
       rc=$?
    else 
       curl --max-time $timeout "http://$serverurl/alive"
       rc=$?
       if [[ $rc == 0 ]]; then
          echo ''
       fi
    fi
    if [[ $rc != 0 ]]; then
       echo "************************ server seems to be down `date` ************************"
       if [[ "$mail" != 'no' ]]; then
          sh $mail
       fi
    fi
    sleep $every
  done
}

# check, whether java and javac are on the PATH. Check for a 64 bit java 7 version.
# if everything is ok, set variable checkJava to '', otherwise the variable contains error messages
function _checkJava {
  checkJava=''
  which java > /dev/null
  if [[ $? != 0 ]]; then
     checkJava=$'  java was NOT found on the PATH.\n'
  fi
  which javac > /dev/null
  if [[ $? != 0 ]]; then
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
    *)        checkJava=${checkJava}$'  This may be a java version other than 1.7.*. A version 7 is recommended.' ;;
  esac
  if [[ "$checkJava" != '' ]]; then
     echo "problems detected:"
     echo "$checkJava"
  fi
  echo 'you are using the following java runtime:'
  echo $javaversion
}

function _exportApplication {
  if [[ "$dbOption" != '-e' &&  "$dbOption" != '-i' ]]; then
    echo 'database option (first parameter) must be either -e or -i - exit 4'
    exit 4
  fi
  if [[ -e "$exportpath" ]]; then
     echo "target directory \"$exportpath\" already exists - exit 4"
     exit 4
  fi
  echo "creating the target directory \"$exportpath\""
  mkdir "$exportpath"
  if [ $? -ne 0 ] ; then
    echo "creating the directory \"$exportpath\" failed - exit 4"
    exit 4
  fi
  if [[ "$dbOption" == '-e' ]]; then
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
# -------------- begin of a here documents --------------------------------------------------
  cat >"${exportpath}/openRoberta.properties" <<.eof
version = ${oraversion}
validversionrange.From = ${oraversion}
validversionrange.To = ${oraversion}
crosscompiler.basedir = userProjects/
crosscompiler.build.xml = crosscompiler-ev3-build.xml
hibernate.connection.url = jdbc:hsqldb:file:db/openroberta-db
robot.updateResources.dir = resources
robot.updateResources.dir = updateResources
robot.crossCompilerResources.dir = crossCompilerResources
.eof
}

function _createemptydb {
  dbpath="$1"
  main='de.fhg.iais.roberta.main.Administration'
  run='java -cp "OpenRobertaServer/target/resources/*" '"${main} createemptydb ${dbpath}"
  echo "executing: $run"
  eval $run
}

function _scpev3menuFn {
  run="scp OpenRobertaServer/target/updateResources/EV3Menu.jar root@${ev3ipaddr}:/home/root/lejos/bin/utils"
  echo "executing: ${run}"
  $run
}

function _setev3serverinfoFn {
  echo ${serverurl} | ssh root@${ev3ipaddr} "cat > /home/roberta/serverIP.txt"
}

function _scpev3libsFn {
  runtime="OpenRobertaServer/target/updateResources/OpenRobertaRuntime.jar"
  shared="OpenRobertaServer/target/updateResources/OpenRobertaShared.jar"
  json='OpenRobertaServer/target/updateResources/json.jar'
  run="ssh root@${ev3ipaddr} mkdir -p /home/roberta/lib"
  echo "executing: ${run}"
  $run
  run="scp ${runtime} ${shared} ${json} root@${ev3ipaddr}:/home/roberta/lib"
  echo "executing: ${run}"
  $run
}

# ---------------------------------------- begin of the script ----------------------------------------------------
cmd="$1"; shift
if [[ "$cmd" == '' || "$cmd" == '--help' || "$cmd" == '-h' ]]; then
   _helpFn
   exit 0
elif [[ "$cmd" == '--java' ]]; then
   _checkJava
   exit 0
fi

continueReadingProperties='true'
while [[ "$continueReadingProperties" == 'true' ]]; do
   case "$cmd" in
      --ev3ipaddr|-ev3ip) ev3ipaddr="$1"; shift;    cmd="$1"; shift ;;
      --serverurl|-sp)    serverurl="$1"; shift;    cmd="$1"; shift ;;
      --version|-v)       oraversion="$1"; shift;   cmd="$1"; shift ;;
      *)                  continueReadingProperties='false' ;;
   esac
done
if [[ "$cmd" == '' ]]; then
   exit 0
fi
case "$cmd" in
--checkout-db|--restore-db)
                    git checkout HEAD -- OpenRobertaServer/db ;;
--sqlclient|-sql)   dbjar=' OpenRobertaServer/target/resources/hsqldb-2.3.2.jar'
                    dbdriver='org.hsqldb.jdbc.JDBCDriver'
                    dburl='jdbc:hsqldb:file:OpenRobertaServer/db/openroberta-db'
                    java -jar $dbjar --driver $dbdriver --url $dburl --user orA --password Pid ;;
--scpev3menu|-menu) _scpev3menuFn ;;
--scpev3libs|-libs) _scpev3libsFn ;;
--setev3serverinfo|-sinfo) _setev3serverinfoFn ;;
--createemptydb)    dbpath="$1"
                    _createemptydb "$dbpath" ;;
--export|-e)        dbOption="$1"
                    exportpath="$2"
                    _exportApplication ;;
--start|-s)         propfile="$1"
                    if [[ "$propfile" != '' ]]; then
                       propfile="file:$propfile" # make a file resource from the path
                    fi
                    _startFn ;;
--alive)            if [[ "$1" == '-q' ]]; then
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
