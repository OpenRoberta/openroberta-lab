#!/bin/bash

isServerNameValid ${SERVER_NAME}
SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
isDirectoryValid $SERVER_DIR_OF_ONE_SERVER

source $SERVER_DIR_OF_ONE_SERVER/decl.sh
isDeclShValid
isDefined BASE_VERSION

case "${GIT_REPO}" in
    /*) : ;;
    *)  GIT_REPO=${BASE_DIR}/git/${GIT_REPO}
esac
isDirectoryValid ${GIT_REPO}
case "$BRANCH" in
    master) DEBUG=true
            question 'you deploy MASTER. Do you have modified the MASTER branch as usual before deployment?' ;;
     *)     : ;;
esac

# ----- AQUIRE A FILE LOCK. Checkout, build binaries, export, build container -----
( flock -w 1200 9 || (echo "$DATE: deployement of ${SERVER_NAME} was delayed for more than 20 minutes. Exit 12"; exit 12) 
    [ "$DEBUG" = 'true' ] && echo "$DATE: got the lock for file '${GIT_DIR}/lockfile'"
    cd ${GIT_REPO}
    if [ "$GIT_PULL_BEFORE_BUILD" == 'false' ]
    then
        echo 'git repo is already uptodate, nothing is pulled, nothing is checked out'
        LAST_COMMIT="git working tree considered UPTODATE. Last commit is $(git rev-list HEAD...HEAD~1)"
    elif [ "$COMMIT" == '' ]
    then
        echo "checking out branch '$BRANCH'. Throw away the complete old state"
        # this will remove files, that may be VALUABLE
        # get all from remote, remove working tree and index files, that are dirty
        git fetch --all
        git reset --hard
        git clean -fd
        # goto a branch different from the one to be checked out. Otherwise the -D will fail. Then checkout the branch, connect to the remote implicitly
        case "$BRANCH" in
          develop) git checkout master ;;
          *)       git checkout develop ;;
        esac
        git branch -D $BRANCH
        git checkout $BRANCH
        # get the last commit for documentation
        LAST_COMMIT=$(git rev-list HEAD...HEAD~1)
    else
        echo "checking out commit '$COMMIT'"
        git checkout -f $COMMIT
        LAST_COMMIT=$COMMIT
    fi
    mvn clean install -DskipTests
    rm -rf $SERVER_DIR_OF_ONE_SERVER/export
    ./ora.sh export $SERVER_DIR_OF_ONE_SERVER/export gzip
    cp ${CONF_DIR}/docker-for-lab/start.sh $SERVER_DIR_OF_ONE_SERVER/export
    cp ${CONF_DIR}/docker-for-lab/admin.sh $SERVER_DIR_OF_ONE_SERVER/export
    chmod ugo+x $SERVER_DIR_OF_ONE_SERVER/export/*.sh
    IMAGE="openroberta/server_${INAME}_$SERVER_NAME:$BASE_VERSION"
    DOCKERRM=$(docker rmi $IMAGE 2>/dev/null)
    case "$DOCKERRM" in
        '') echo "found no docker image '$IMAGE' to remove. That is ok." ;;
        * ) echo "removed docker image '$IMAGE'"
    esac
    docker build --no-cache --build-arg BASE_VERSION=$BASE_VERSION -f ${CONF_DIR}/docker-for-lab/DockerfileLab -t $IMAGE $SERVER_DIR_OF_ONE_SERVER/export
    
    case "$BRANCH" in
        master) MVN_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
                docker tag $IMAGE "openroberta/server_${INAME}_$SERVER_NAME:${MVN_VERSION}" ;;
        *)      ;;
    esac
    
    DATE_DEPLOY=$(date --rfc-3339=seconds)
    cat >>$SERVER_DIR_OF_ONE_SERVER/history.txt <<.EOF
==================================
HOSTNAME = $HOSTNAME
DATE_SETUP = $DATE_SETUP
DATE_DEPLOY = $DATE_DEPLOY
GIT_REPO = ${GIT_REPO}
BRANCH = $BRANCH
LAST_COMMIT = $LAST_COMMIT
PORT = $PORT
.EOF

) 9>${GIT_DIR}/lockfile
