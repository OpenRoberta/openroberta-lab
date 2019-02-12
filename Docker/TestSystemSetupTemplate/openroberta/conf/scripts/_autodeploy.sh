#!/bin/bash

DEBUG=true
DATE=$(date '+%Y-%m-%d %H:%M:%S')
if [ -f "$SERVER/autodeploy.txt" ]
then
    flock -n 9 9>$SERVER/lockfile
    RC=$?
    case "$RC" in
        0)  [ "$DEBUG" = 'true' ] && echo "$DATE: got the lock for file '$SERVER/lockfile'"
            AUTODEPLOY_SERVERS=$(cat $SERVER/autodeploy.txt)
            set $AUTODEPLOY_SERVERS
            for SERVER_NAME do
                [ "$DEBUG" = 'true' ] && echo "$DATE: checking server '$SERVER_NAME'"
                isServerNameValid $SERVER_NAME
                SERVERDIR=$SERVER/$SERVER_NAME
                isDirectoryValid $SERVERDIR
                cd $SERVERDIR
                source ./decl.sh
                if [ "$GIT_UPTODATE" == 'true' ]
                then
                    echo "$DATE: for server '$SERVER_NAME' GIT_UPTODATE is true. This makes no sense"
                fi
                [ "$DEBUG" = 'true' ] && echo "$DATE: for server '$SERVER_NAME' checking branch '$BRANCH'"
                cd $GIT_REPO
                git checkout .
                git fetch
                git checkout $BRANCH
                DIFF=$(git rev-list HEAD...origin/$BRANCH --count)
                [ "$DEBUG" = 'true' ] && echo "$DATE: found $DIFF new commits in branch '$BRANCH'"
                if [ $DIFF -gt 0 ]
                then
                    echo "$DIFF new commits found in branch $BRANCH. Thus the server $SERVER_NAME will be deployed"
                    $SCRIPTDIR/run.sh deploy $SERVER_NAME
                fi
            done ;; 
        *) echo "$DATE: '$SERVER/lockfile' was LOCKED. Trying again later" ;;
    esac
else
    [ "$DEBUG" = 'true' ] && echo "$DATE: no file '$SERVER/autodeploy.txt' found. Nothing to do"
fi
