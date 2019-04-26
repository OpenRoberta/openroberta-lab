# Operating Instructions for the Test and Prod Server using DOCKER container (2019-04-19 15:00:00)

At least to read:

* section "(re-)create base container": how to build the base image of everything else.
* section "Operating Instructions for the Test and Prod Server": how to use docker to run the server(s)

# (re-)create the base image (done from time to time)

## generate the "base" IMAGE. This image contains the crosscompiler and the crosscompiler resources (header files, ...).

The docker image "base" is used as basis for further images. It contains all software needed by the crosscompilers, i.e.

* the crosscompiler binaries itself. They are installed by calling `apt`
* header etc. to use together with the cross compiler. They are copied from a clone of the git repository `ora-cc-rsc`.

_Note:_ If the git repository `ora-cc-rsc` is changed, the base image and all images built upon the base image must be rebuilt. This doesn't
occur often. But better do not forget.

```bash
REPO=/data/openroberta/git/openroberta-lab
CC_RESOURCES=/data/openroberta/git/ora-cc-rsc
cd $CC_RESOURCES
docker build -f $REPO/Docker/meta/DockerfileBase_ubuntu_18_04 -t rbudde/openroberta_base:2 .
docker push rbudde/openroberta_base:2
```

## generate the base for INTEGRATION TEST and DEBUG.

Using the configuration file DockerfileIT_* you create an image, built upon the "base" image, that contains all crosscompiler, mvn and git.
It has executed a git clone of the main git repository `openroberta-lab` and has executed a `mvn clean install`. This is done to fill the
(mvn) cache and speeds up later builds considerably. The entrypoint is defined as the bash script "runIT.sh".
If called, it will checkout a branch and runs both the tests and the integration tests.

```bash
REPO=/data/openroberta/git/openroberta-lab
BRANCH=develop
cd $REPO/Docker
docker build -t rbudde/openroberta_it_ubuntu_18_04:2 -f testing/DockerfileIT_ubuntu_18_04 . --build-arg BRANCH=$BRANCH
docker push rbudde/openroberta_it_ubuntu_18_04:2
```

For debug you want to run an image, built upon the "base" image, that contains all crosscompiler, mvn and git.
It has executed a git clone of the main git repository `openroberta-lab` and has executed a `mvn clean install`. This is done to fill the
(mvn) cache and speeds up later builds considerably. The entrypoint is "/bin/bash". This image is build by

```bash
REPO=/data/openroberta/git/openroberta-lab
cd $REPO/Docker
docker build -t rbudde/openroberta_debug_ubuntu_18_04:2 -f testing/DockerfileDebug_ubuntu_18_04 .
docker push rbudde/openroberta_debug_ubuntu_18_04:2
```

# Operating Instructions for the Test and Prod Server

## Overview

The test server serves many different instances of the openroberta lab server. The prod server runs the prod version of the openroberta lab server.
They are setup in the same way. The following text describes the test server setup, the prod setup is the same, but uses only the server named `master`.

All running instances are generated from a branch or a commit of a git repository.

* Each instance of the openroberta lab server is running in a docker container of its own.
* Each instance of the openroberta lab server is connected to a database dedicated to this openroberta lab server. All databases are published by one
  database server running in a docker container of its own.

If you want to deploy the `develop` branch on server `dev4`, for instance, do the following (the variables used are explained below):

* modify in the directory `$SERVER_DIR/dev4` the file `decl.sh` as you need it (set the branch name, e.g.)
* be sure, that in `$DATABASE_DIR/dev4` a valid database is available. Check whether `$DATABASE_DIR/databases.txt` contains `dev4`. It not, add it and restart
  the database server by `$SCRIPT_DIR/run.sh startDbC`

Assuming that the network `ora-net` is created, the database container is running, then you (re-)deploy `dev4` by executing

```bash
$SCRIPT_DIR/run.sh deploy dev4 # 'deploy dev4' is a shorthand for 'gen dev4' and 'start dev4'
$SCRIPT_DIR/run.sh info        # show the state of all server
```

## Apache2 configuration

We need a running web server to distribute requests to the different openroberta lab servers. This is done with Apache (nginx would be fine, too).
Configuration examples exist in the directory apache2. On the test server:

* `localhost:1999` is configured by `test.open-roberta.org.443` and `test.open-roberta.org.80`
* `localhost:1998` is configured by `dev.open-roberta.org.443`  and `dev.open-roberta.org.80`
* `localhost:1997` is configured by `dev1.open-roberta.org.443` and `dev1.open-roberta.org.80`

and so on. B.t.w.: the ports are not fixed. But they must be consistent between apache2 and lab server. On the prod server

* `localhost:1999` is configured by `lab.open-roberta.org.443` and `lab.open-roberta.org.80`

## General structure

All data is stored relative to a directory, usually `/data/openroberta`. We use the following abbreviations:

```bash
BASE_DIR=/data/openroberta
CONF_DIR=$BASE_DIR/conf
SCRIPT_DIR=$CONF_DIR/scripts
SERVER_DIR=$BASE_DIR/server
DATABASE_DIR=$BASE_DIR/db
```

## The openroberta lab servers

The ports `1999` to `1989` are used by up to 11 openroberta lab server. Each of these servers is based on an embedded jetty.
The names of the servers are (b.t.w.: this matches the virtual host names of the apache configuration):

* test: configuration is in `$SERVER_DIR/test/decl.sh`
* dev: configuration is in `$SERVER_DIR/dev/decl.sh`
* dev1 up to dev9: configuration is in `$SERVER_DIR/dev1/decl.sh` up to `$SERVER_DIR/dev9/decl.sh`

The name of the docker images and the name of the running containers are:

* test: image is `rbudde/openroberta_lab_test:2` and container has name `test`
* dev: image is `rbudde/openroberta_lab_dev:2` and container has name `dev`
* dev1 up to dev9: images are `rbudde/openroberta_lab_dev1:2` to `rbudde/openroberta_lab_dev9:2` and container have names `dev1` to `dev9`

Generating the images is done by using the data from `decl.sh`. Generation will:

* pull actual data into a Git repo and checkout the branch/commit as declared (you can suppress this step by setting `GIT_UPTODATE=true`)
* run `mvn clean install -DskipTests`
* export the server into an export directory
* create the docker image from that directory

Note: by using `flock` the generation of images is done sequentially. A new request for image generation is blocked as long as another
generation is running. So it is safe, that many test server share one git repository. This increases the performance of server generation a lot.

## database server

each jetty server needs connection to a Hsqldb-based database. The database URI's match the openroberta lab server names:

* `jdbc:hsqldb:hsql://localhost/openroberta-db-test`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev1` to `jdbc:hsqldb:hsql://localhost/openroberta-db-dev9`

These databases are served by one database server. The database files are located at (and must be supplied by YOU!):

* `$DATABASE_DIR/test`
* `$DATABASE_DIR/dev`
* `$DATABASE_DIR/dev1` to `$BASE/db/dev9`

The database server is deployed in a docker container with name `ora-db-server` created from the docker image `rbudde/openroberta_db:2.4.0`.
The database server is listening to the port `$DATABASE_SERVER_PORT` as defined in `$SCRIPT_DIR/__defs.sh`. This is `9001` by default.

## Scripting

Everything is done with the help of the shell script `$SCRIPT_DIR/run.sh`. I tried to make these scripts as robust as possible. Please send any
problems, improvements, ideas to reinhard.budde at iais.fraunhofer.de

The main commands of the script are (run `$SCRIPT_DIR/run.sh help` to see the names) the following. Note, that if the server name is missing,
_all_ servers found in file `$SERVER_DIR/servers.txt` are taken into account:

* `gen <name>`: generate the docker image for server <name>. Use the configuration found in `$SERVER_DIR/<name>`.
* `start [<name>]`: start a container with the image generated for server <name>. Before doing that, stop a container running sever <name>
* `stop [<name>]`: stop a container for server <name>. Remove the exited container.
* `deploy [<name>]`: shortcut for gen and start for server <name>.

Rarely used are commands:

* `genDbC`: generate the database container `rbudde/openroberta_db:2.4.0`. Used once when the test server is setup.
* `startDbC`: (re-)start the database container servicing the databases, whose names are found in file `$DATABASE_DIR/databases.txt`
* `stopDbC`: stop the database container
* `prune`: remove all stale data from the docker installation. IMPORTANT to call, if you are low with disc space.

Getting information:

* `info`: show images and running containers
* `network`: show the ora-net network
* `logs`: show the last 10 lines of all logs of all running containers. B.t.w.: `docker logs -f <name>` is convenient to see how a server works.
* of course, docker commands can help, too: `docker logs -f dev1` or `docker ps`

## Init scripts and automatic deploy

The shell script `$SCRIPT_DIR/run.sh` has two more commands, that are used to initialize the test setup (on server restart, for example) and to
deploy one or more servers automatically if new commits hit the remote repo.
 
* `autoDeploy`: usually called from cron. Reads the server names from file `$SERVER_DIR/autodeploy.txt` and re-deploys each server, if the git
  repository connected to this server has got new commits. Use `crontab -e` to add the following line to the crontab to run this is:
  
```bash
*/5 * * * * bash /data/openroberta/conf/scripts/run.sh -q autoDeploy >>/data/openroberta/logs/cronlog.txt
```

* `startAll` and `stopAll`: usually called from the configuration file `robertalab` found in `/etc/init.d`. A typical script is:

```bash
#!/bin/sh
### BEGIN INIT INFO
# Provides:          robertalab
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: openroberta service
# Description:       Start, stop and restart the database server and the openroberta server declared in server/servers.txt
### END INIT INFO

# Author: Reinhard

BASE=/data/openroberta
SCRIPTS=$BASE/conf/scripts
case "$1" in
    start)   bash $SCRIPTS/run.sh -q startAll                              >>/data/openroberta/logs/init.txt ;;
    stop)    bash $SCRIPTS/run.sh -q stopAll                               >>/data/openroberta/logs/init.txt ;;
    restart) bash $SCRIPTS/run.sh -q startAll                              >>/data/openroberta/logs/init.txt ;;
    *)       echo "invalid command \"$1\". Usage: $0 {start|stop|restart}" >>/data/openroberta/logs/init.txt
             exit 12 ;;
esac
```

Assuming systemd, after putting `robertalab` into `/etc/init.d`, the service is added with the commands:

```bash
chmod ugo+x /etc/init.d/robertalab

systemctl daemon-reload
systemctl enable robertalab
systemctl start robertalab

systemctl status robertalab       # to see logging
journalctl -u robertalab.service  # to see more logging
```

# seldom used functioanlity: run the integration tests and start a debug container

## integration tests

The integration test container clones a branch, executes all tests, including the integration tests and
in case of success it returns 0, in case of errors/failures it returns 16

```bash
export BRANCH='develop'
docker run rbudde/openroberta_it_ubuntu_18_04:2 $BRANCH 1.2.3 # 1.2.3 is the db version and unused for tests
```

## debug container

```bash
docker run -p 7100:1999 -it --entrypoint /bin/bash rbudde/openroberta_debug_ubuntu_18_04:2
```

It starts a /bin/bash and you probably will either run a server:

```bash
git checkout develop; git pull;
mvn clean install -DskipTests
./ora.sh --createEmptydb
./ora.sh --start-from-git
```

or you want to run the integration tests (but many other tasks are possible :-)

```bash
git checkout develop; git pull; git co anotherBranchToDebug
mvn clean install -PrunIT
```

# deprecated functionality: create the server, database, upgrade and embedded images in a docker container and run them

this functionality is deprecated. It may be re-used later. See the directory `TestSystemSetupTemplate` for a much more flexible test setup.

## generate the "gen" image. This image can generate an OpenRoberta distribution.

When the docker image "gen" is run, it GENERATES an OpenRoberta distribution. It is NO OpenRoberta distribution by itself.
It gets version numbers independent from the OpenRoberta versions. During image creation a maven build is executed for
branch develop to fill the /root/.m2 cache. This makes later builds much faster.

```bash
REPO=/data/openroberta/git/robertalab
cd $REPO/Docker
docker build -f meta/DockerfileGen_ubuntu_18_04 -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1
```

## define the variables used (set as needed!):

this is a setting usable on the test machine:

```bash
export HOME="/data/openroberta
export VERSION='3.2.2'
export BRANCH='develop'
export GITREPO="$HOME/git/robertalab"
export DB_PARENTDIR="$HOME/db"
export SERVER_PORT_ON_HOST=1999
export DBSERVER_PORT_ON_HOST=9001
export BUILD_ALL=true
```

## Run the "gen" image. It will generate images

* fetches the branch declared as first parameter
* and generate images for the version given as second parameter (after mvn install, export, ...)
  * "openroberta_lab" contains a server ready to co-operate with a db server
  * "openroberta_db" contains a production-ready db server
  * "openroberta_upgrade" contains an administration service working with an embedded database to upgrade the database
  * "openroberta_embedded" contains a server working with an embedded database

The first -v arguments makes the "real" docker demon available in the "gen" container. Do not change this parameter.

```bash
docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $BRANCH $VERSION $BUILD_ALL
```
	   
## use the images: Upgrading the database

Assume that the exported environment variable DB_PARENTDIR contains a valid data base directory, e.g. db-$VERSION,
then run the upgrader first, if a new version is deployed (running it, if nothing has to be updated, is a noop):

```bash
docker run -v $DB_PARENTDIR:/opt/db rbudde/openroberta_upgrade:$BRANCH-$VERSION
```

## use the images: embedded server

Start the server with an embedded database (no sqlclient access during operation, otherwise fine) either with
docker or with docker-compose (using compose for a single container may appear a bit over-engineered, but is preferred).

```bash
docker run -p 7100:1999 -v $DB_PARENTDIR:/opt/db rbudde/openroberta_embedded:$BRANCH-$VERSION &

cd $GITREPO/Docker
docker-compose -p ora -f dc-embedded.yml up -d &
```

If the log message is printed, which tells you how many programs are in the data base, everything is fine and you can
access the server at http://dns-name-or-localhost:7100 (see docker command and the compose file)

## use the images: server and database server

Running two container, one db server container and one server container is the preferred way for productive systems.
It allows the access to the database with a sql client (querying, but also backup and checkpoints):

```bash
  cd $GITREPO/Docker
  docker-compose -p ora -f dc-server-db-server.yml up -d
```

Remove the "-d" flag and append " &" to see detailed logging. Otherwise use docker logs to see logging output.
To stop the service, run

```bash
cd $GITREPO/Docker
docker-compose -p ora -f dc-server-db-server.yml stop
```

If you want to run two instances of the lab at the same time (do you really want to do this?), you start compose two times
and give each compose instance a different name. Of course you'll need two databases. Stop is straight-forward.
For the two services two different networks are created (inspect the output of "docker network ls"), IP ranges are separated (inspect
the output of "docker network inspect ora1_default" resp "docker network inspect ora2_default")

```bash
cd $GITREPO/Docker
SERVER_PORT_ON_HOST=7301 DBSERVER_PORT_ON_HOST=9301 DB_PARENTDIR=/tmp/ora1 docker-compose -p ora1 -f dc-server-db-server.yml up -d
SERVER_PORT_ON_HOST=7302 DBSERVER_PORT_ON_HOST=9302 DB_PARENTDIR=/tmp/ora2 docker-compose -p ora2 -f dc-server-db-server.yml up -d
```

Note: when the container terminate, the message "... exited with code 130" is no error, but signals termination with CTRL-C

