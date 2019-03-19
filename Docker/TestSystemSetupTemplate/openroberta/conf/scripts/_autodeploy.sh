#!/bin/bash

if [ -f "$SERVER/autodeploy.txt" ]
then
    flock -n 9 9>$SERVER/lockfile
    RC=$?
    case "$RC" in
        0)  [ "$DEBUG" = 'true' ] && echo "$DATE: got the lock for file '$SERVER/lockfile'"
            AUTODEPLOY_SERVERS=$(cat $SERVER/autodeploy.txt)
            AUTODEPLOY_SERVERS=$(echo $AUTODEPLOY_SERVERS)
            if [ "$AUTODEPLOY_SERVERS" = '' ]
            then
                [ "$DEBUG" = 'true' ] && echo "$DATE: empty file '$SERVER/autodeploy.txt' found. Nothing to do"
            else
                set $AUTODEPLOY_SERVERS
                UPDATED_SERVERS=$((0))
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
                    case "$GIT_REPO" in
                        /*) : ;;
                        *)  GIT_REPO=$BASE/$GIT_REPO
                    esac
                    cd $GIT_REPO
                    git checkout -f .
                    git fetch
                    GITMSG=$(git checkout -f $BRANCH 2>&1)
                    [ "$DEBUG" = 'true' ] && echo "$DATE: git messages are: $GITMSG"
                    DIFF=$(git rev-list HEAD...origin/$BRANCH --count)
                    [ "$DEBUG" = 'true' ] && echo "$DATE: found $DIFF new commits in branch '$BRANCH'"
                    if [ $DIFF -gt 0 ]
                    then
                        echo "$DATE: $DIFF new commits found in branch $BRANCH. A reset --hard to origin/$BRANCH is executed"
                        git reset --hard origin/$BRANCH
                        echo "$DATE: the server $SERVER_NAME will be deployed now"
                        $SCRIPTDIR/run.sh deploy $SERVER_NAME
                        UPDATED_SERVERS=$(($UPDATED_SERVERS + 1))
                    fi
                done
                if [ $UPDATED_SERVERS -gt 0 ]
                then
                    [ "$DEBUG" = 'true' ] && echo "$UPDATED_SERVERS server(s) have been updated. Removing stale docker data now"
                    $SCRIPTDIR/run.sh -q prune
                fi
            fi ;; 
        *) echo "$DATE: '$SERVER/lockfile' was LOCKED. Trying again later" ;;
    esac
else
    [ "$DEBUG" = 'true' ] && echo "$DATE: no file '$SERVER/autodeploy.txt' found. Nothing to do"
fi
