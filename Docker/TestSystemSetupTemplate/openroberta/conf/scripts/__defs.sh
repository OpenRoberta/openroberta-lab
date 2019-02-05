#!/bin/bash

function isDefined {
    VAR="$1"; shift
    VAL="${!VAR}"
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

function setServerNamesintoSERVER_NAMES {
    if [ "$1" == '' ]
    then
        if [ -f "$SERVER/servers.txt" ]
        then
            SERVERS=$(cat $SERVER/servers.txt)
            SERVER_NAMES=$SERVERS
        else
            echo "no file '$SERVER/servers.txt' found. Exit 12"
            exit 12
        fi
    else
        SERVER_NAMES=$1
    fi
    for SERVER_NAME in $SERVER_NAMES; do
        isServerNameValid $SERVER_NAME
    done
}    

HOSTNAME=$(hostname)
echo "working on host $HOSTNAME"

BASE=/data/openroberta
CONF=$BASE/conf
SERVER=$BASE/server

isDirectoryValid $BASE
isDirectoryValid $CONF
isDirectoryValid $SERVER
