BASE_VERSION=''   # the version number of the docker base image, FROM which the docker image of this server is derived.
                  # Take the highest number found at `https://hub.docker.com/r/openroberta/base-x64/tags`
# TAG_VERSION=''  # OPTIONAL parameter. ONLY for starting container. If defined, take this tag to be started. Replaces the default tag 'BASE_VERSION'. Emergency feature.

PORT=''                              # port the lab server will listen to, e.g. '1999' or '8080'
LOG_LEVEL='ERROR'                    # the logging level of the root logger. From DEBUG to ERROR
LOG_CONFIG_FILE='/logback-prod.xml'  # logback configuration, use vvv

REMOTE_DEBUG=false         # OPTIONAL parameter. Prepare the container and the JVM running in the docker container to connect to a debugger on port 2000. Illegal for master for obvious reasons.
GC_LOGGING=false           # OPTIONAL parameter. Activate logging for the garbage collector

BRANCH=''                  # the branch to be deployed on this server, e.g. 'develop'
# COMMIT=''                # alternative to BRANCH. If a commit instead of a branch is deployed, e.g. '174db1b4f0'
GIT_REPO=''                # the git repo where the branch has to be checked out, found in the 'git' directory, e.g. 'openroberta-lab'
GIT_PULL_BEFORE_BUILD=true # set it to 'true'! Set to 'false' ONLY, if you have a well-prepared, ready to use branch checked out in a repository in the
                           # 'git' directory, that is used exclusively for this server instant. Setting to 'false' is very dangerous!

# START_ARGS='-d key1=val1 -d key2=val2 -D robot3:key3=val3 -D robot3:key3=val3 '
                           # used to supply parameter when the container is started. Used often to define the robot plugins to be published. May be empty.
                           # -d replaces properties found in 'openroberta.properties'
                           # -D replaces properties found in <robot>.properties from a robot plugin
