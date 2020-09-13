#!/bin/bash

if [ "${AUTODEPLOY}" != '' ]
then
    flock -n 9 9>${GIT_DIR}/lockfile
    RC=$?
    case "${RC}" in
        0)  [ "${DEBUG}" = 'true' ] && echo "${DATE}: got the lock for file '${GIT_DIR}/lockfile'"
            UPDATED_SERVERS=$((0))
            for SERVER_NAME in ${AUTODEPLOY}
            do
                [ "${DEBUG}" = 'true' ] && echo "${DATE}: checking server '${SERVER_NAME}'"
                isServerNameValid ${SERVER_NAME}
                SERVER_DIR_OF_ONE_SERVER=${SERVER_DIR}/${SERVER_NAME}
                isDirectoryValid ${SERVER_DIR_OF_ONE_SERVER}
                cd ${SERVER_DIR_OF_ONE_SERVER}
                source ./decl.sh
                isDeclShValid
                if [ "${GIT_PULL_BEFORE_BUILD}" == 'false' ]
                then
                    echo "${DATE}: for server '${SERVER_NAME}' GIT_PULL_BEFORE_BUILD is false. This makes no sense with auto-deploy. Exit 12"
                    exit 12
                fi
                [ "${DEBUG}" = 'true' ] && echo "${DATE}: for server '${SERVER_NAME}' checking branch '${BRANCH}'"
                case "${GIT_REPO}" in
                    /*) : ;;
                    *)  GIT_REPO=${BASE_DIR}/git/${GIT_REPO}
                esac
                cd ${GIT_REPO}
                git checkout -f .
                git fetch
                GITMSG=$(git checkout -f ${BRANCH} 2>&1)
                [ "${DEBUG}" = 'true' ] && echo "${DATE}: git messages are: ${GITMSG}"
                DIFF=$(git rev-list HEAD...origin/${BRANCH} --count)
                [ "${DEBUG}" = 'true' ] && echo "${DATE}: found ${DIFF} new commits in branch '${BRANCH}'"
                if [ ${DIFF} -gt 0 ]
                then
                    echo "${DATE}: ${DIFF} new commits found in branch ${BRANCH}. A reset --hard to origin/${BRANCH} is executed"
                    git reset --hard origin/${BRANCH}
                    echo "${DATE}: the server ${SERVER_NAME} will be deployed now"
                    ${SCRIPT_MAIN}/run.sh -q deploy ${SERVER_NAME}
                    UPDATED_SERVERS=$((${UPDATED_SERVERS} + 1))
                fi
            done
            if [ ${UPDATED_SERVERS} -gt 0 ]
            then
                [ "${DEBUG}" = 'true' ] && echo "${UPDATED_SERVERS} server(s) have been updated. Removing stale docker data now"
                ${SCRIPT_MAIN}/run.sh -q prune
            fi ;; 
        *) echo "${DATE}: '${GIT_DIR}/lockfile' was LOCKED. Nothing done. Autodeploy will be rescheduled later and then try again." ;;
    esac
else
    [ "${DEBUG}" = 'true' ] && echo "${DATE}: no server in variable AUTODEPLY found. Nothing to do"
fi
