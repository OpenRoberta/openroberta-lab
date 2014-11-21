#!/bin/bash

ipaddr='10.0.1.1'
oraversion='1.0.0-SNAPSHOT'

function helpFn {
  echo 'THIS SCRIPT IS FOR DEVELOPERS, WHO PULLED THE GIT-REPOSITORY AND WORK WITH THE OpenRoberta ARTIFACT';
  echo 'use: ora.sh --CMD1 OPT_PARM1 --CMD2 OPT_PARM2 ... ...';
  echo '';
  echo 'you may customize the script by defining default values. Values are now:';
  echo 'ipaddr='"$ipaddr";
  echo 'oraversion='"$oraversion";
  echo '';
  echo 'miscelleneous commands';
  echo '';
  echo '  --help                             get help';
  echo '  --ipaddr {IP-ADDR}                 set the ip addr of the EV3 brick for further commands';
  echo '  --version {ORA-VERSION}            set the version of the OpenRoberta artifact for further commands';
  echo '';
  echo 'commands available after a successful mvn clean install';
  echo '';
  echo '  --start {PROPERTY-FILE}            start of the server, optionally supply a property file';
  echo '                                     the default property file is in OpenRobertaServer/src/main/resources';
  echo '                                     a java7 installation is required, java+javac must be in the path' ;
  echo '  --scpev3menu                       scp the ev3menu.jar to the EV3, uses ipaddr';
  echo '                                     the root password is "", thus hit return if you are asked';
  echo '  --scpev3libs                       scp libraries to the EV3, uses ipaddr and version';
  echo '                                     the root password is "", thus hit return everytime you are asked';
  echo '  --createemptydb PATH-TO-DB         create an empty database with all tables needed for the OpenRoberta server'
  echo '                                     Needs a file name. Creates files and a directory with this name AS PREFIX.'
  echo '                                     if the database exists, it is not recreated. If a table "PROGRAM" is found'
  echo '                                     in the database, it is expected, that the setup was already done.'
  echo '  --export PATH-TO-INSTALLATION-DIR  create a self-contained installation with an empty database'
  echo '                                     The installation contains a top level start script "start.sh",'
  echo '                                     that can be used without parameter or with a proerty file as parameter.'
}

function startFn {
  propfile="$1"
  serverjar="target/OpenRobertaServer-${oraversion}.jar"
  main='de.fhg.iais.roberta.main.ServerStarter'
  run="java -cp ${serverjar} ${main} ${propfile}"
  echo "executing: $run"
  cd OpenRobertaServer
  $run
}

function exportApplication {
  exportpath="$1"
  if [[ -e "$exportpath-qqqqqq" ]]; then
     echo "target directory \"$exportpath\" already exists - exit 4"
     exit 4
  fi
  echo "creating the target directory \"$exportpath\""
  mkdir "$exportpath"
  echo "creating an empty data base"
  $0 --createemptydb "${exportpath}/db/openroberta-db"
  serverjar="OpenRobertaServer-${oraversion}.jar"
  webresources="OpenRobertaServer/staticResources"
  echo "copying the jar"
  cp "OpenRobertaServer/target/$serverjar" "$exportpath"
  echo "copying the web resources"
  echo cp -r "$webresources" "$exportpath"
  echo 'creating the start script "start.sh"'
  cat >"${exportpath}/start.sh" <<.eof
#!/bin/bash
run="java -cp $serverjar de.fhg.iais.roberta.main.ServerStarter \$1"
echo "executing: \$run"
\$run
.eof
}

function createemptydb {
  dbpath="$1"
  serverjar="OpenRobertaServer/target/OpenRobertaServer-${oraversion}.jar"
  main='de.fhg.iais.roberta.main.Administration'
  run="java -cp ${serverjar} ${main} createemptydb ${dbpath}"
  echo "executing: $run"
  $run
}

function scpev3menuFn {
  run="scp EV3Menu/dist/EV3Menu.jar root@${ipaddr}:/home/root/lejos/bin/utils"
  echo "executing: ${run}"
  $run
}

function scpev3libsFn {
  runtime="OpenRobertaRuntime/target/OpenRobertaRuntime-${oraversion}.jar"
  shared="OpenRobertaShared/target/OpenRobertaShared-${oraversion}.jar"
  json='EV3Menu/lib/json-20140107.jar'
  run="ssh root@${ipaddr} mkdir -p /home/roberta/lib"
  echo "executing: ${run}"
  $run
  run="scp ${runtime} ${shared} ${json} root@${ipaddr}:/home/roberta/lib"
  echo "executing: ${run}"
  $run
}

# ---------------------------------------- begin of the script ----------------------------------------------------
cmd="$1"; shift

if [[ "$cmd" == '' ]]; then
   helpFn
   exit 0
fi
while [[ "$cmd" != '' ]]; do
   case "$cmd" in
   --help|-h)          helpFn ;;
   --ipaddr|-i)        ipaddr="$1"; shift ;;
   --version|-v)       oraversion="$1"; shift ;;
   --scpev3menu|-m)    scpev3menuFn ;;
   --scpev3libs|-l)    scpev3libsFn ;;
   --createemptydb|-c) dbpath="$1"; shift
                       createemptydb "$dbpath" ;;
   --export|-e)        exportpath="$1"; shift
                       exportApplication "$exportpath" ;;
   --start|-s)         cmd="$1"; shift
                       if [[ $cmd != '' && $cmd != -* ]]; then
                          propfile="file:$cmd"
                          cmd="$1"; shift
                       fi
                       startFn "$propfile"
                       continue ;; # because cmd is already set
   *)                  echo 'invalid or no parameter: "'"$cmd"'"'
                       echo ''
                       helpFn
                       exit 4 ;;
   esac

   cmd="$1" # here all commands w.o. parameters arrive. Thus cmd has to be set
   shift
done

exit 0
