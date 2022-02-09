#!/bin/bash

QUIET='no'
while true
do
  case "$1" in
    -q)        QUIET='yes'
               shift ;;
    *)         break ;;
  esac
done

if [ "$QUIET" = 'no' ]
then
  echo 'THIS SCRIPT EXECUTES COMMANDS WITHIN A DOCKER CONTAINER. ERRORS MAY CRASH THE OPENROBERTA SERVER.'
  echo 'admin.sh [-q] <CMD>'
  echo '-q quiet mode'
  echo ''
  echo '<CMD>s are:'
  echo '  example           example script (currently no commands are in use)'
fi

# get the command
CMD="$1"; shift

RC=0
case "$CMD" in
  example)  echo 'use a bash on the server running our server in a docker container (e.g. server "test")'
            echo 'excute in this bash the command ".../run.sh admin test example". Then:'
            echo 'the run script will call INSIDE of the docker container of server "test" the script "admin.sh"'
            echo 'with the only parameter "example". This in turn will output the echos you are reading :-)'
            ;;
# ----------------------------------------------------------------------------------------------------------------------------------------
  '')               echo "no command. Script terminates"
                    RC=12 ;;
  *)                echo "invalid admin command. Ignored: \"$CMD\""
                    RC=12 ;;
esac

if [[ "$RC" -ne 0 ]]
then
  echo '*** the command did NOT succeed (completely). Look into console output and logfiles. $(date) ***'
fi
exit 0
