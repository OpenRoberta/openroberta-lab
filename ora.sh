#!/bin/bash

function _mkAndCheckDir {
  mkdir -p "$1"
  if [ $? -ne 0 ]
  then
    echo "creating the directory \"$1\" failed - exit 12"
    exit 12
  fi
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
  echo "server version: ${serverVersion}"
  
  echo "copying all jars"
  _mkAndCheckDir "${exportpath}/lib"
  cp OpenRobertaServer/target/resources/*.jar "$exportpath/lib"

  echo 'copying the staticResources'
  cp -r OpenRobertaServer/staticResources ${exportpath}/staticResources
  case "$gzip" in
    'n') echo 'staticResources are NOT gzip-ped. This increases load times when the internet connection is slow. Use for debug only.' ;;
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
  
  echo 'script for starting the server is copied'
  cp admin.sh admin-help.txt ${exportpath}
  chmod ugo+rx admin.sh
}

# ---------------------------------------- begin of the script ----------------------------------------------------
if [ ! -d OpenRobertaServer ]
then
  echo 'please start this script from the root of the Git working tree - exit 12'
  exit 12
fi

# the following settings fit for 'export' and 'start-from-git' 
DB_PARENTDIR=./OpenRobertaServer/db-embedded
DB_NAME=openroberta-db
JAVA_LIB_DIR='OpenRobertaServer/target/resources' # created by mvn install ...
ADMIN_DIR='./admin'
CC_RESOURCE_DIR='../ora-cc-rsc'
QUIET='no'
XMX=''
RDBG=''
 
while true
do
  case "$1" in
    -dbParentdir)  DB_PARENTDIR=$2
                   shift; shift ;;
    -dbName)       DB_NAME=$2
                   shift; shift ;;
    -java-lib-dir) JAVA_LIB_DIR=$2
                   shift; shift ;;
    -admin-dir)    ADMIN_DIR=$2
                   shift; shift ;;
    -oraccrsc)     CC_RESOURCE_DIR=$2
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
ADMIN_CLASS='de.fhg.iais.roberta.main.Administration'

cmd="$1"
shift

case "$cmd" in
export) _export $* ;;

''|help|-h)     cat ora-help.txt ;;

start-from-git) if [[ ! -d $DB_PARENTDIR ]]; then 
                   echo "No database found. An empty database will be created."
                   java -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.Administration create-empty-db jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME
                fi
                java $RDBG -cp ${JAVA_LIB_DIR}/\* de.fhg.iais.roberta.main.ServerStarter \
                     -d database.mode=embedded \
                     -d database.parentdir=$DB_PARENTDIR \
                     -d database.name=$DB_NAME \
                     -d server.staticresources.dir=OpenRobertaServer/staticResources \
                     -d robot.crosscompiler.resourcebase="$CC_RESOURCE_DIR" \
                     $* ;;

new-docker-setup) base_dir="$1"
                if [[ -d $base_dir ]]
                then
                  echo "basedir '$base_dir' exists. Exit 12"
                  exit 12
                fi
                cp -r Docker/openroberta $base_dir
                cp Docker/_README.md $base_dir
                echo "New docker setup created in $base_dir. Edit 'decl.sh' and setup databases and servers now."
                echo "Please read the first paragraph in `Docker/_README.md` !" ;;

new-server-in-docker-setup)
                base_dir="$1"
                server_name="$2"
                if [[ ! -d $base_dir || ! -f $base_dir/decl.sh ]]
                then
                  echo "basedir '$base_dir' is no valid dir for docker setup. Exit 12"
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
                echo "New server $server_name created. Edit '$base_dir/decl.sh' and '$server_dir/decl.sh'"
                echo "Copy an existing database or create an empty database after 'mvn clean install -DskipTests'"
                echo "by calling from this repo './admin.sh -dbParentdir $base_dir/db/$server_name create-empty-db' " ;;

update-docker-setup)
                base_dir="$1"
                if [[ ! -d $base_dir || ! -f $base_dir/decl.sh ]]
                then
                  echo "basedir '$base_dir' no valid dir for docker setup. Exit 12"
                  exit 12
                fi
                rm -rf $base_dir/conf $base_dir/scripts
                cp -r Docker/openroberta/conf $base_dir/conf
                cp -r Docker/openroberta/scripts $base_dir/scripts
                cp Docker/_README.md $base_dir
                echo "configuration data copied to $base_dir/conf and $base_dir/scripts" ;;

check-xss)      # unused
                exit 12
                databaseurl="jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME"
                java -cp ${JAVA_LIB_DIR}/\* "$ADMIN_CLASS" check-xss "$databaseurl" ;;
                  
renameRobot)    # unused
                exit 12
                databaseurl="jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME"
                java -cp ${JAVA_LIB_DIR}/\* "$ADMIN_CLASS" rename "$databaseurl" $2 $3;;
                  
configurationCleanUp) #unused
                exit 12
                databaseurl="jdbc:hsqldb:file:$DB_PARENTDIR/$DB_NAME"
                java -cp ${JAVA_LIB_DIR}/\* "$ADMIN_CLASS" configuration-clean-up "$databaseurl" ;;

*)              echo "invalid command: $cmd - exit 1"
                exit 1 ;;
esac

exit 0
