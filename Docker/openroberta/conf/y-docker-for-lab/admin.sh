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
  cleanup-temp-user-dirs) # the rm -rf is scary. Should be really made robust against errors. -mtime +1 is: older than 1 day
                    AGE='-mtime +1'
                    TMP_DIR='/tmp/openrobertaTmp'
                    cd $TMP_DIR
                    WD=$(pwd)
                    if [[ "$WD" == $TMP_DIR ]]
                    then
                        find . -ignore_readdir_race -maxdepth 1 ! -name '.' ! -name '..' $AGE -exec rm -rf -- {} \;
                        RC=$?
                    else
                        echo "cd $TMP_DIR did not succeed. This is dangerous! Analyse!"
                        RC=12
                    fi ;;
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
