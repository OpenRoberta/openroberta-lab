#!/bin/bash

# ask a question. If the user answers "y", everything is fine. Otherwise exit 12
function question {
  if [ "${YES}" == 'true' ]
  then
    echo "-yes was set. Automatic 'y' for question: $1"
  else
    echo -n "$1 (\"y\" if ok) "
    local ANSWER
    read ANSWER
    case "${ANSWER}" in
      y) : ;;
      *) exit 12 ;;
    esac
  fi
}

function isDefined {
    local VAR="$1"; shift
    local VAL="${!VAR}"
    if [ "${VAL}" == '' ]
    then
        echo "variable ${VAR} is undefined or empty. Exit 12"
        exit 12
    elif [ "$1" == '' ]
    then
        return
    fi
    for ENUM do
        if [ "${VAL}" == "${ENUM}" ]
        then
            return
        fi
    done
    echo "variable ${VAR} is not one of the enumerations '$*'. Exit 12"
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
    isDefined PORT
    isDefined LOG_LEVEL DEBUG INFO WARN ERROR
    isDefined LOG_CONFIG_FILE
    isDefined GIT_REPO
    isDefined GIT_PULL_BEFORE_BUILD true false
    # COMMIT or BRANCH must be defined
    if [[ "${BRANCH}" == '' && "${COMMIT}" == '' ]]
    then
        echo "variable BRANCH and COMMIT are both undefined or empty. Exit 12"
        exit 12
    fi
}

function getOS {
    OS=${OSTYPE//[0-9.-]*/}
    case "$OS" in
      linux)  echo "linux" ;;
      darwin) echo "macos" ;;
      msys)   echo "win" ;;
      *)      echo "UNKNOWN" ;;
    esac
}

function isWin {
    [ $(getOS) == 'win' ]
}

function isLinux {
    [ $(getOS) == 'linux' ]
}

isDirectoryValid "${BASE_DIR}"
isDirectoryValid "${SCRIPT_HELPER}" # defined in "run.sh", must be valid (otherwise this check couldn't be executed)
isFileValid "${BASE_DIR}/decl.sh"

CONF_DIR=${BASE_DIR}/conf
SERVER_DIR=${BASE_DIR}/server
GIT_DIR=${BASE_DIR}/git
DATABASE_DIR=${BASE_DIR}/db
DB_ADMIN_DIR=${BASE_DIR}/db/dbAdmin

isDirectoryValid ${CONF_DIR}
isDirectoryValid ${SERVER_DIR}
isDirectoryValid ${GIT_DIR}
isDirectoryValid ${DATABASE_DIR}
isDirectoryValid ${DB_ADMIN_DIR}

isDefined DATABASE_SERVER_PORT
isDefined DOCKER_NETWORK_NAME

case $(uname -m) in # get the architecture
  x*)   ARCH=x64 ;;
  arm*) ARCH=arm32v7 ;;
  *)    echo "works on x86 and arm32v7 architecture only. Exit 12"
        exit 12 ;;
esac

isDefined ALIVE_ACTIVE
case ${ALIVE_ACTIVE} in
  true)  isDefined ALIVE_MAIL_SMTP_SERVER
         isDefined ALIVE_MAIL_SMTP_PORT
         isDefined ALIVE_MAIL_SENDER
         isDefined ALIVE_MAIL_RECEIVER ;;
  false) : ;;
  *)     echo "variable ALIVE_ACTIVE is '${ALIVE_ACTIVE}' and not either true or false. Exit 12"
         exit 12 ;;
esac

isDefined PYTHON
WHICH_PYTHON=$(which ${PYTHON})
if [[ "${WHICH_PYTHON}" == '' ]]
then
    echo "variable PYTHON does not point to a Python binary. Log file analysis will NOT work"
else
    VERSION_PYTHON=$(${PYTHON} -V)
    case "${VERSION_PYTHON}" in
        Python\ 3*) : ;;
        Python\ 2*) echo "variable PYTHON seems to point to a Python2 binary. Log file analysis will NOT work" ;;
        *) echo "could not get version information from Python installation. Log file analysis will NOT work" ;;
    esac
fi

for NAME in ${SERVERS}; do
    isServerNameValid "${NAME}"
done
for NAME in ${AUTODEPLOY}; do
    isServerNameValid "${NAME}"
done
for NAME in ${DATABASES}; do
    isServerNameValid "${NAME}"
done
isDefined DATABASEXMX
case "${DATABASEXMX}" in
  -Xmx*) : ;;
  *)     echo "variable DATABASEXMX is '${DATABASEXMX}' and not like '-Xmx4G' for example. Exit 12"
         exit 12 ;;
esac

HOSTNAME=$(hostname)
if [ "${QUIET}" != true ]
then
    echo "working on host ${HOSTNAME} at BASE_DIR ${BASE_DIR}"
fi

export DATE=$(date '+%Y-%m-%d %H:%M:%S')
export STAR='********************'
export STAR_DATE_STAR="${STAR} ${DATE} ${STAR}"

function headerMessage {
    echo "${STAR} ${DATE} $1 ${STAR}"
}

export -f headerMessage
