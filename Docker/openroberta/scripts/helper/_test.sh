#!/bin/bash

CMD="$1"; shift

echo "test command ${CMD} executed at ${DATE}"
case "${CMD}" in
    lock)   echo 'aquire a lock for 60 sec'
            ( flock -w 60 9
                  echo 'starting to sleep for 60 sec'
                  sleep 60
                  echo 'awoke after 60 sec. Releasing the lock'
            ) 9>${SERVER_DIR}/lockfile || echo "lock was delayed for more than 60 sec. Why?" ;;
    check)  echo 'check if somebody locked'
            flock -n 9 9>${GIT_DIR}/lockfile
            RC=$?
            case "${RC}" in
                0) echo "was NOT LOCKED" ;;
                *) echo "was LOCKED" ;;
            esac ;;
    *)      echo "invalid test command: ${CMD}"
esac
