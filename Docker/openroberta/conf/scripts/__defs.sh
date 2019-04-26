#!/bin/bash

# ask a question. If the user answers "y", everything is fine. Otherwise exit 12
function question {
  echo -n "$1 (\"y\" if ok) "
  local ANSWER
  read ANSWER
  case "$ANSWER" in
    y) : ;;
    *) exit 12 ;;
  esac
}

function isDefined {
    local VAR="$1"; shift
    local VAL="${!VAR}"
    if [ "$VAL" == '' ]
    then
        echo "variable $VAR is not defined in server configuration file 'decl.sh'. Exit 12"
        exit 12
    elif [ "$1" == '' ]
    then
        return
    fi
    for ENUM do
        if [ "$VAL" == "$ENUM" ]
        then
            return
        fi
    done
    echo "variable $VAR from 'decl.sh' is not one of the enumerations '$*'. Exit 12"
    exit 12
}

function isServerNameValid {
    case "$1" in
        master)   : ;;
        test|dev) : ;;
        dev[1-9]) : ;;
        *       ) echo "invalid name. Value must be 'master', 'test', 'dev', 'dev1'..'dev9', but is '$1'. Exit 12"
                  exit 12 ;;
    esac
}

function isDirectoryValid {
    if [ ! -d $1 ]
    then
        echo "the directory '$1' is invalid. Exit 12"
        exit 12
    fi
}

function serversDOTtxt2SERVER_NAMES {
    if [ -f "$SERVER_DIR/servers.txt" ]
    then
        local SERVERS=$(cat $SERVER_DIR/servers.txt)
        SERVER_NAMES=$SERVERS
    else
        echo "$SERVER_DIR/servers.txt is missing. Exit 12"
        exit 12
    fi
}    

HOSTNAME=$(hostname)
if [ "$QUIET" != true ]
then
    echo "working on host $HOSTNAME"
fi

BASE_DIR=/data/openroberta
CONF_DIR=$BASE_DIR/conf
SCRIPT_DIR=$CONF_DIR/scripts
SERVER_DIR=$BASE_DIR/server
DATABASE_DIR=$BASE_DIR/db
LOG_DIR=$BASE_DIR/logs

isDirectoryValid $BASE_DIR
isDirectoryValid $CONF_DIR
isDirectoryValid $SCRIPT_DIR
isDirectoryValid $SERVER_DIR
isDirectoryValid $DATABASE_DIR
isDirectoryValid $LOG_DIR

DATABASE_SERVER_PORT=9001
