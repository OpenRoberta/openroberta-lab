#!/bin/bash

# admin script

URI='jdbc:hsqldb:hsql://localhost/openroberta-db'

LOGFILE='./dbAdmin.log'
QUIET='no'
XMX=''
while true
do
	case "$1" in
	  -log)  LOGFILE=$2
			 shift; shift ;;
	  -q)    QUIET='yes'
			 shift ;;
	  -Xmx*) XMX=$1
	         shift ;;
	  *)	 break ;;
	esac
done

if [ "$QUIET" = 'no' ]
then
	echo 'admin.sh [-q] [-log <path-to-log-file>] -Xmx<size>'
	echo '  --backup                   access the database server and create a backup in directory dbBackup'
	echo '  --shutdown [db-uri]        access the database and issue a "shutdown compact" command'
	echo '                             It is expected, that the lab runs in db server mode and not in embedded mode.'
	echo '                             If not, supply the full uri of the database. Put the uri into single quotes to avoid globbing'
	echo '                             - server mode (not needed, because this is the default): "jdbc:hsqldb:hsql://localhost/openroberta-db"'
	echo '                             - embedded mode: "jdbc:hsqldb:file:./db-x.y.z/openroberta-db;ifexists=true" for version x.y.z'
	echo '  --checkpoint [-d] [db-uri] access the database and issue a "checkpoint" command. Pay attention to the notes about --shutdown!'
	echo '                             the optional -d forces defragmentation and takes a lot more time to finish'
	echo '  --sqlclient [db-uri]       read SELECT commands from the terminal and execute them. Pay attention to the notes about --shutdown!'
	echo '  --sqlclient <SQL> [db-uri] execute the well-quoted <SQL> command'
	echo '  --upgrade [db-parent-dir]  upgrade the database, if necessary. The database is accessed in embedded mode.'
	echo '                             No server or db server must be running. The actual version is retrieved from the installation'
	echo '  --version                  print the server version (may be suffixed with -SNAPSHOT) and terminate'
	echo '  --version-for-db           print the database version (never contains -SNAPSHOT) and terminate'

	echo "logging into $LOGFILE"
fi

CMD="$1"; shift
case "$CMD" in
  '--backup')            java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration dbBackup $URI >>$LOGFILE 2>&1 ;;
  '--shutdown')          case "$1" in
                           '') : ;;
                           *)  URI="$1"; shift ;;
                         esac
                         echo "shutdown the database accessable at $URI"
                         java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration dbShutdown "$URI" >>$LOGFILE 2>&1 ;;
  '--checkpoint')        case "$1" in
                           '-d') PARAM=$1; shift ;;
                           *)    PARAM='' ;;
                         esac
						 case "$1" in
                           '') : ;;
                           *)  URI="$1"; shift ;;
                         esac
                         echo "checkpoint for the database accessable at $URI"
                         java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration dbCheckpoint "$PARAM" "$URI" >>$LOGFILE 2>&1 ;;
  '--sqlclient')         case "$1" in
                           '') : ;;
                           *)  URI="$1"; shift ;;
                         esac
                         echo "sql client for database accessable at $URI. Type commands, terminate with <return>"
                         java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration sqlclient "$URI" ;;
  '--sql')   		     SQL="$1"; shift
						 case "$1" in
                           '') : ;;
                           *)  URI="$1"; shift ;;
                         esac
                         echo "execute one sql statement using $URI"
                         java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration sql "$URI" "$SQL" ;; # note: parameter URI + SQL exchanged
  '--upgrade')           DB_VERSION=$(java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db)
				         case "$1" in
				           '') echo 'database parent directory is missing - exit 1'
				               exit 1 ;;
				           *)  DB_PARENTDIR="$1"; shift ;;
				         esac
                         java $XMX -cp lib/\* de.fhg.iais.roberta.main.Administration upgrade "$DB_PARENTDIR" >>$LOGFILE 2>&1 ;;
  '--version')           java -cp lib/\* de.fhg.iais.roberta.main.Administration version ;;
  '--version-for-db')    java -cp lib/\* de.fhg.iais.roberta.main.Administration version-for-db ;;
  '')                    echo "no command. Termination" ;;
  *)                     echo "invalid command ignored: \"$CMD\"" ;;
esac
