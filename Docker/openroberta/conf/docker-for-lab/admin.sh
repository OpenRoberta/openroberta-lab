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
  echo 'THIS SCRIPT EXECUTES DANGEROUS COMMANDS. MISUSE MAY CRASH THE OPENROBERTA SERVER.'
  echo 'admin.sh [-q] <CMD>'
  echo '-q quiet mode'
  echo ''
  echo '<CMD>s are:'
  echo '  cleanup-temp-user-dirs  temporary directories allocated for crosscompilers are removed, if they are older than 1d'
fi

# get the command
CMD="$1"; shift

RC=0
case "$CMD" in
  cleanup-temp-user-dirs) # the rm -rf is scary. Should be really made robust against errors.
                    age='-mtime +1'
                    cd /tmp
                    wd=${PWD}
                    if [[ "$wd" == '/tmp' ]]
                    then
                        find * -maxdepth 1 $age -exec rm -rf -- {} \;
                        RC=$?
                    else
                        echo 'cd /tmp did not succeed. This is dangerous! Analyse!'
                        RC=12
                    fi ;;
# ----------------------------------------------------------------------------------------------------------------------------------------
  '')               echo "no command. Script terminates"
                    RC=12 ;;
  *)                echo "invalid admin command. Ignored: \"$CMD\""
                    RC=12 ;;
esac

if [ $RC -ne 0 ]
then
  echo '*** the command did NOT succeed. Look into console output and logfiles ***'
fi