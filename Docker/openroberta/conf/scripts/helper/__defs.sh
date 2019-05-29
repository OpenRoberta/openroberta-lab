#!/bin/bash

# ask a question. If the user answers "y", everything is fine. Otherwise exit 12
function question {
  if [ "$SYSTEMCALL" == 'true' ]
  then
    echo "SYSTEMCALL. Automatic 'y' for question: $1"
  else
    echo -n "$1 (\"y\" if ok) "
    local ANSWER
    read ANSWER
    case "$ANSWER" in
      y) : ;;
      *) exit 12 ;;
    esac
  fi
}

function isDefined {
    local VAR="$1"; shift
    local VAL="${!VAR}"
    if [ "$VAL" == '' ]
    then
        echo "variable $VAR is undefined or empty. Exit 12"
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
    echo "variable $VAR is not one of the enumerations '$*'. Exit 12"
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

function isFileValid {
    if [ ! -f $1 ]
    then
        echo "file '$1' not found. Exit 12"
        exit 12
    fi
}


function isDeclShValid { 
    isDefined DATE_SETUP
    isDefined PORT
    isDefined LOG_LEVEL DEBUG INFO WARN ERROR
    isDefined LOG_CONFIG_FILE
    isDefined GIT_REPO
    isDefined GIT_UPTODATE true false
    # COMMIT or BRANCH must be defined
    if [[ "$BRANCH" == '' && "$COMMIT" == '' ]]
    then
        echo "variable BRANCH and COMMIT are both undefined or empty. Exit 12"
        exit 12
    fi
}

isDirectoryValid "${BASE_DIR}"
isDirectoryValid "${SCRIPT_HELPER}" # defined in "run.sh", must be valid (otherwise this check couldn't be executed)
isFileValid "${BASE_DIR}/config.sh"

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

for NAME in $SERVERS; do
    isServerNameValid "$NAME"
done
for NAME in $AUTODEPLOY; do
    isServerNameValid "$NAME"
done
for NAME in $DATABASES; do
    isServerNameValid "$NAME"
done

HOSTNAME=$(hostname)
if [ "$QUIET" != true ]
then
    echo "working on host ${HOSTNAME} for installation ${INAME}"
fi