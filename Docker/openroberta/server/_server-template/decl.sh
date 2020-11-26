BASE_VERSION=''            # the version of the docker base image, FROM which this server image is derived. Read and get the number from the 'Docker/_README.md' file.
TAG_VERSION=''             # OPTIONAL parameter. ONLY for starting container. If defined, take this as the tag to be started. It replaces the default tag 'BASE_VERSION'. BE CAREFUL.

PORT=''                    # port the jetty server will listen to, very often 1999
LOG_LEVEL=''               # the logging level of the root logger. From DEBUG to ERROR
LOG_CONFIG_FILE=''         # logback configuration, very often /logback-prod.xml or /logback.xml

REMOTE_DEBUG=false         # prepare the container and the JVM running in the docker container to connect to a debugger on port 8000. Illegal for master for obvious reasons.
GC_LOGGING=false           # activate logging for the garbage collector

BRANCH=''                  # the branch to be deployed on this server, e.g. 'develop'
# COMMIT=''                # if a commit instead of a branch is deployed, e.g. '174db1b4f0'
GIT_REPO=''                # the git repo where the branch is founf, e.g. 'openroberta-lab'
GIT_PULL_BEFORE_BUILD=true # set to false ONLY, if you have a well-prepared, ready to use branch checked out. NEVER use such a git repo for other server deployments. NEVER.

# START_ARGS='-d key1=val1 -d key2=val2' # use to supply parameter when container is started
