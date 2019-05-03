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

isDirectoryValid ${BASE_DIR}
isDirectoryValid ${SCRIPT_DIR} # defined in "run.sh"

CONF_DIR=${BASE_DIR}/conf
SERVER_DIR=${BASE_DIR}/server
GIT_DIR=${BASE_DIR}/git
DATABASE_DIR=${BASE_DIR}/db
DB_ADMIN_DIR=${BASE_DIR}/db/dbAdmin

isDirectoryValid ${CONF_DIR}
isDirectoryValid ${SERVER_DIR}
isDirectoryValid ${GIT_DIR}
isDirectoryValid ${DATABASE_DIR}
isDirectoryValid $DB_ADMIN_DIR

isDefined INAME
isDefined DATABASE_SERVER_PORT
isDefined DOCKER_NETWORK_NAME

isDefined SERVERS
isDefined AUTODEPLOY
isDefined DATABASES

HOSTNAME=$(hostname)
if [ "$QUIET" != true ]
then
    echo "working on host ${HOSTNAME} for installation ${INAME}"
fi