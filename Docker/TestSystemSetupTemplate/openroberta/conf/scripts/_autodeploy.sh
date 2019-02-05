#!/bin/bash

if [ -f "$SERVER/autodeploy.txt" ]
then
    flock -n 9 9>$SERVER/lockfile
    RC=$?
    case "$RC" in
        0)  AUTODEPLOY_SERVERS=$(cat $SERVER/autodeploy.txt)
            set $AUTODEPLOY_SERVERS
            for SERVER_NAME do
                isServerNameValid $SERVER_NAME
                SERVERDIR=$SERVER/$SERVER_NAME
                isDirectoryValid $SERVERDIR
                cd $SERVERDIR
                source ./decl.sh
                if [ "$GIT_UPTODATE" == 'true' ]
                then
                    echo "for server '$SERVER_NAME' GIT_UPTODATE is true. This makes no sense"
                fi
        
                cd $GIT_REPO
                git checkout .
                git fetch
                DIFF=$(git rev-list HEAD...origin/develop --count)
                if [ $DIFF -gt 0 ]
                then
                    echo "$DIFF new commits found. Deploying server $SERVER_NAME"
                    $SCRIPTDIR/run.sh deploy $SERVER_NAME
                fi
            done ;; 
        *) echo "'$SERVER/lockfile' was LOCKED. Trying again later" ;;
    esac
fi
