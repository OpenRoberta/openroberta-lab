BASE_VERSION=''           # the version of the docker base image, FROM which this server image is derived. Be careful to define the RIGHT number

PORT=''                    # port the jetty server will listen to, very often 1999
LOG_LEVEL=''               # the logging level of the root logger. From DEBUG to ERROR
LOG_CONFIG_FILE=''         # logback configuration, very often /logback-prod.xml or /logback.xml

BRANCH=''                  # the branch to be deployed on this server, e.g. 'develop'
# COMMIT=''                # if a commit instead of a branch is deployed, e.g. '174db1b4f0'
GIT_REPO=''                # the git repo where the branch is founf, e.g. 'openroberta-lab'
GIT_PULL_BEFORE_BUILD=true # set to false ONLY, if you have a well-prepared, ready to use branch checked out. NEVER use such a git repo for other server deployments. NEVER.

# START_ARGS='-d key1=val1 -d key2=val2' # use to supply parameter when container is started
