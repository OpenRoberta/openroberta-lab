#!/bin/bash

function isDefined {
    VAR=$1
    if [ "${!VAR}" == '' ]
    then
        echo "variable $VAR is not defined in server configuration file 'decl.sh'. Exit 12"
        exit 12
    fi
}

function isServerNameValid {
    case "$1" in
        test|dev|dev[1-9]) : ;;
        *                ) echo "invalid name. Value must be 'test','dev','dev1'..'dev9', but is '$1'. Exit 12"
                           exit 12;;
    esac
}

function isDirectoryValid {
    if [ ! -d $1 ]
then
    echo "the directory '$1' is invalid. Exit 12"
    exit 12
fi
}

HOSTNAME=$(hostname -f)
echo "working on host $HOSTNAME"

BASE=/data/openroberta
CONF=$BASE/conf
SERVER=$BASE/server

isDirectoryValid $BASE
isDirectoryValid $CONF
isDirectoryValid $SERVER
