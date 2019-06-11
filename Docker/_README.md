# Operating Instructions for the Test and Prod Server using DOCKER container (2019-04-19 15:00:00)

At least to read:

* section "(re-)create base container": how to build the base image of everything else.
* section "Operating Instructions for the Test and Prod Server": how to use docker to run the server(s)

# (re-)create the base image (done from time to time)

## generate the "base" IMAGE. This image contains the crosscompiler and the crosscompiler resources (header files, ...).

The docker image "base" is used as basis for further images. It contains all software needed by the crosscompilers, i.e.

* the crosscompiler binaries itself. They are installed by calling `apt`
* header etc. to use together with the cross compiler. They are copied from a clone of the git repository `ora-cc-rsc`.
* openroberta helper libraries for lejos, nao and raspberryPi.

_Note:_ If the git repository `ora-cc-rsc` is changed, the base image and all images built upon the base image must be rebuilt. This doesn't
occur often. But better do not forget.

```bash
REPO=/data/openroberta-lab/git/openroberta-lab
CC_RESOURCES=/data/openroberta-lab/git/ora-cc-rsc
cd $CC_RESOURCES

git checkout develop; git pull
git checkout master; git pull
echo 'is a "git merge develop" needed?'

mvn clean install
docker build --no-cache -t rbudde/openroberta_base:2 -f $REPO/Docker/meta/DockerfileBase_ubuntu_18_04 .
docker push rbudde/openroberta_base:2
```

## generate the image for INTEGRATION TEST.

Using the configuration file DockerfileIT_* you create an image, built upon the "base" image, that contains all crosscompiler, mvn and git.
It has executed a git clone of the main git repository `openroberta-lab` and has executed a `mvn clean install`. This is done to fill the
(mvn) cache and speeds up later builds considerably. The entrypoint is defined as the bash script "runIT.sh".
If called, it will checkout a branch and runs both the tests and the integration tests.

```bash
REPO=/data/openroberta-lab/git/openroberta-lab
BRANCH=develop
cd $REPO/Docker/testing
docker build --no-cache -t rbudde/openroberta_it_ubuntu_18_04:2 -f DockerfileIT_ubuntu_18_04 . --build-arg BRANCH=$BRANCH
docker push rbudde/openroberta_it_ubuntu_18_04:2
```

# Operating Instructions for the Test and Prod Server

Our test server serves many different instances of the openroberta lab server. Our prod server runs the prod version of the openroberta lab server.
They are setup in the same way. The following text describes the test server setup (the prod setup is the same, but uses the server named `master`).

## Directory structure

The template for this framework is contained in directory `Docker/openroberta`. It contains the directories

* conf - contains an example of an apache2 configuration and 2 dirs `docker-*`, used to generate docker images for the db and the jetty server
* conf/scripts - contains shell scripts to administrate the framework. The main script id `run.sh`. Call it w.o. parameter to get help.
  the directory `helper` contains scripts, that are sourced from `run.sh` and do the "real" work.
* git - here one or more git repos, used to generate the openroberta server instances, are contained. At least one git repo is needed, usually a clone
  from https://github.com/OpenRoberta/openroberta-lab.git
* server - contains the servers. Each server has a name (master,test,dev,dev1...dev9), which is the name of a directory. This directory stores all
  data to configure the server in file `decl.sh`. In directory `export` all artifacts making up the server are stored during server
  generation. In directory `admin` all logging data and the tutorials are stored.
  Each server has an associated docker image (and usually a running container), whose name is essentially the server's name.
* db - contains the databases. Each database has a name (master,test,dev,dev1...dev9) matching the name of the jetty server who needs this data.
  the name of the database is the name of a directory, which in turn contains all database files. All databases are served by one Hsqldb server instance.
  Directory `dbAdmin` stores logging data and all database backups (when created :-)
  There is one docker image `rbudde/openroberta_db_server` and usually a running container, whose name is essentially `ora-db-server`.

## Overview

All running openroberta instances are generated from a branch or a commit of a git repository.

* Each instance of the openroberta lab server is running in a docker container of its own.
* Each instance of the openroberta lab server is connected to a database dedicated to this openroberta lab server. All databases are published by one
  database server running in a docker container of its own.

If you want to deploy the `develop` branch on server `dev4`, for instance, do the following (the variables used are explained below):

* make sure, that in the directory `$SERVER_DIR/dev4` the file `decl.sh` contains the expected data (branch name, port number for accessing the server, e.g.)
* be sure, that in `$DATABASE_DIR/dev4` a valid database is available. Check whether `decl.sh` contains `dev4` in variable `DATABASES`. It not,
  add it and restart the database server by `$SCRIPT_DIR/run.sh start-dbc`

All container of an openroberta test instance use a bridge network with (unique!) name `$DOCKER_NETWORK_NAME`.
Assuming that the network is created, the database container is running, then you may (re-)deploy `dev4` by executing

```bash
$SCRIPT_DIR/run.sh deploy dev4 # 'deploy dev4' is a shorthand for 'gen dev4' and 'start dev4'
docker ps                      # the container for dev4 should be running
```

## Apache2 configuration

We need a running web server to distribute requests to the different openroberta lab servers. This is done with Apache (nginx would be fine, too).
Configuration examples exist in the directory apache2. On the test server:

* `localhost:1999` is configured by `test.open-roberta.org.443` and `test.open-roberta.org.80`
* `localhost:1998` is configured by `dev.open-roberta.org.443`  and `dev.open-roberta.org.80`
* `localhost:1997` is configured by `dev1.open-roberta.org.443` and `dev1.open-roberta.org.80`

and so on. B.t.w.: the ports are not fixed. But they must be consistent between apache2 and lab server. For defaults see the listing below. On the prod server

* `localhost:1999` is configured by `lab.open-roberta.org.443` and `lab.open-roberta.org.80`

## Conventions

All data is stored relative to a base directory (your choice, we use `/data/openroberta-lab`). Abbreviations:

```bash
# BASE_DIR is the directory, in which the configuration file 'decl.sh' is found
CONF_DIR=$BASE_DIR/conf
SCRIPT_DIR=$CONF_DIR/scripts
SERVER_DIR=$BASE_DIR/server
DATABASE_DIR=$BASE_DIR/db
```

The whole setup has a unique name `$INAME`, defined in `decl.sh`.

## The openroberta lab servers

The ports `1999` to `1989` are used by up to 11 openroberta server. Each of these servers is based on an embedded jetty.
The names of the servers are (b.t.w.: this matches the virtual host names of the apache configuration):

* test: configuration is in `$SERVER_DIR/master/decl.sh` (usually port 1999)
* dev: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1998)
* dev1: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1997)
* dev2: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1996)
* dev3: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1995)
* dev4: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1994)
* dev5: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1993)
* dev6: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1992)
* dev7: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1991)
* dev8: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1990)
* dev9: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1989)

The name of the docker images and the name of the running containers are:

* test: image is `rbudde/openroberta_${INAME}_test:2` and container has name `${INAME}-test`
* dev: image is `rbudde/openroberta_${INAME}_dev:2` and container has name `${INAME}-dev`
* dev1 up to dev9: images are `rbudde/openroberta_${INAME}_dev1:2` to `rbudde/openroberta_${INAME}_dev9:2` and
  container have names `${INAME}-dev1` to `${INAME}-dev9`

Generating the images is done by using the data from `decl.sh` from the server directory. Generation will:

* pull actual data into a Git repo and checkout the branch/commit as declared
  (you can suppress this step by setting `GIT_UPTODATE=true`)
* run `mvn clean install -DskipTests`
* export the server into the export directory
* create the docker image from that directory

Note: by using `flock` the generation of images is done sequentially. A new request for image generation is blocked as long as another
generation is running. So it is safe, that many test server share one git repository. This increases the performance of server generation a lot. If the branch `master` is deployed, we advise to use one git repo exclusively for this (relevant :-) branch

## database server

each jetty server needs connection to a Hsqldb-based database. The database URI's match the openroberta lab server names:

* `jdbc:hsqldb:hsql://localhost/openroberta-db-test`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev1` to `jdbc:hsqldb:hsql://localhost/openroberta-db-dev9`

These databases are served by one database server. The database files are located at (and must be supplied by YOU!):

* `$DATABASE_DIR/test`
* `$DATABASE_DIR/dev`
* `$DATABASE_DIR/dev1` to `$BASE/db/dev9`

The database server is deployed in a docker container with name `ora-${INAME}-db-server` created from the docker image `rbudde/openroberta-${INAME}-db-server:2.4.0`.
The database server is listening to the port `$DATABASE_SERVER_PORT` as defined in `config.sh`. This is often the
Hsqldb default `9001`.

## Scripting

Everything is done with the help of the shell script `$SCRIPT_DIR/run.sh`. I tried to make these scripts as robust as possible. Please send any
problems, improvements, ideas to reinhard.budde at iais.fraunhofer.de

The main commands of the script can be seen if you run `$SCRIPT_DIR/run.sh` without parameter. The following sequence of commands is advised:
* clone the openroberta-lab repository into directory `git`.
* generate the bridge network for this instance of the framework (the name is defined in `config.sh`).
* create the databases needed in directory `db` and put their names into `config.sh`.
* start the database server. Have a look at the log (`docker ps; docker logs <name-of-container>`) to check for errors.
* setup your servers in `server/<server-name>/decl.sh` and put their names into `config.sh`. Note, that `test, dev, dev1...dev9` are the
  only names accepted as server names.
* generate and start your servers `$SCRIPT_DIR/run.sh deploy <server-name>`. Have a look at the log (`docker ps; docker logs <name-of-container>`)
  to check for errors.
* enable autodeploy (see below).
* add init functionality for server (re-)boots (see below).

## Automatic deploy, database backup, removing temporary files and init scripts

The shell script `$SCRIPT_DIR/run.sh` has commands, that are used to administrate the framework.
 
* `auto-deploy`: usually called from cron. It takes server names from variable `AUTODEPLOY` from `config.sh` and re-deploys each server, if the git
  repository connected to this server has got new commits. Use `crontab -e` to add the following line to the crontab to look for commits every 5 minutes:
  
```bash
*/5 * * * * bash <SCRIPT_DIR>/scripts/run.sh -q auto-deploy >><BASE_DIR>/logs/cronlog.txt
```

* `backup`: usually called from cron. It takes a database name and creates a database backup in the `dbAdmin` directory.
  Use `crontab -e` to add the following line to the crontab to generate a database backup every night at 2 o'clock:
  
```bash
0 2 * * * bash <SCRIPT_DIR>/run.sh -q backup <database-name> >><BASE_DIR>/logs/cronlog.txt

```

* `cleanup-temp-user-dirs`: usually called from cron. It takes a server name and runs a shell in the corresponding container, that will remove temporary
  old data allocated by the cross compiler. It is assumed, that these crosscompiler-allocated files are used not longer than one day after their creation.
  Use `crontab -e` to add the following line to the crontab to remove garbage every night 20 minutes after 2:
  
```bash
20 2 * * * bash <SCRIPT_DIR>/run.sh -q admin <server-name> cleanup-temp-user-dirs >><BASE_DIR>/logs/cronlog.txt
```

* `start-all` and `stop-all`: usually called from the configuration file `openrobertalab` found in `/etc/init.d`. A typical script is:

```bash
#!/bin/sh
### BEGIN INIT INFO
# Provides:          openrobertalab
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: openrobertalab service
# Description:       Start, stop and restart the database server and the openroberta server declared in server/servers.txt
### END INIT INFO

# Author: Reinhard

BASE_DIR=<BASE_DIR>
SCRIPTS=$BASE_DIR/conf/scripts
export SYSTEMCALL=true
case "$1" in
    start)   bash $SCRIPTS/run.sh -q start-all                             >>$BASE_DIR/logs/init.txt ;;
    stop)    bash $SCRIPTS/run.sh -q stop-all                              >>$BASE_DIR/logs/init.txt ;;
    restart) bash $SCRIPTS/run.sh -q stop-all                              >>$BASE_DIR/logs/init.txt
             bash $SCRIPTS/run.sh -q start-all                             >>$BASE_DIR/logs/init.txt ;;
    *)       echo "invalid command \"$1\". Usage: $0 {start|stop|restart}" >>$BASE_DIR/logs/init.txt
             exit 12 ;;
esac
```

If the variable `SYSTEMCALL` is set to true, it is assumed, that `run.sh` is called from a system service. All security
questions, that are asked in interactive mode, are then answered with `y`.

Assuming systemd, after putting `openrobertalab` into `/etc/init.d`, the service is added with the commands:

```bash
chmod ugo+x /etc/init.d/openrobertalab

systemctl daemon-reload
systemctl enable openrobertalab
systemctl start openrobertalab

systemctl status openrobertalab       # to see logging
journalctl -u openrobertalab.service  # to see more logging
```

## Note about the -d command line arguments for the openrobertalab server container

The global properties needed for the openrobertalab server are found in resource `/openroberta.properties`. At start time these parameter can be modified
by an arbitrary number of command line arguments like `-d KEY=VALUE`. The final list of these `-d` arguments is build as follows:

* script `start.sh`: the shell script `start.sh` is inside the openrobertalab server container and starts the JVM with main class `ServerStarter`.
  It contains some `-d` arguments, that are the
  same for _all_ openrobertalab server container and adds more `-d` arguments (passed by the script that starts the container) by adding `$*`
* script `_start.sh`: the docker container is started (via the main script `run.sh`) by the bash statements found in `_start.sh`. Here more `-d` statements are added,
  that refer to the OpenRoberta server instance (master, test, dev). They adds database names, network names, logging level, logging configuration and mount points.
  Furthermore the script adds as the last level of `-d` additions those founds invariable `START_ARGS`
* variable `START_ARGS`: every OpenRoberta server has a configuration file `decl.sh`. Here the shell variable `START_ARGS` can define more `-d` arguments.
  *NOTE:* this is the place for the last deployer-defined additions as declaring the list of robot plugins to use, whether this is the public server or not, and so on.
  It is possible, but _not_ adviced to overwrite properties already defined at the two places described above

# Run the integration tests

The integration test container clones a branch, executes all tests, including the integration tests and
in case of success it returns 0, in case of errors/failures it returns 16

```bash
export BRANCH='develop'
docker run rbudde/openroberta_it_ubuntu_18_04:2 $BRANCH 1.2.3 # 1.2.3 is the db version and unused for tests
```

# deprecated functionality: create the server, database, upgrade and embedded images in a docker container and run them

this functionality is deprecated. It may be re-used later. See the directory `TestSystemSetupTemplate` for a much more flexible test setup.

## generate the image for DEBUG (rarely used)

For debug you want to run an image, built upon the "base" image, that contains all crosscompiler, mvn and git.
It has executed a git clone of the main git repository `openroberta-lab` and has executed a `mvn clean install`. This is done to fill the
(mvn) cache and speeds up later builds considerably. The entrypoint is "/bin/bash". This image is build by

```bash
REPO=/data/openroberta-lab/git/openroberta-lab
cd $REPO/Docker
docker build -t rbudde/openroberta_debug_ubuntu_18_04:2 -f testing/DockerfileDebug_ubuntu_18_04 .
docker push rbudde/openroberta_debug_ubuntu_18_04:2
```

## run the DEBUG container (rarely used)

```bash
docker run -p 7100:1999 -it --entrypoint /bin/bash rbudde/openroberta_debug_ubuntu_18_04:2
```

It starts a /bin/bash and you probably will either run a server:

```bash
git checkout develop; git pull;
mvn clean install -DskipTests
./ora.sh create-empty-db
./ora.sh start-from-git
```

or you want to run the integration tests (but many other tasks are possible :-)

```bash
git checkout develop; git pull; git co anotherBranchToDebug
mvn clean install -PrunIT
```

## generate the "gen" image. This image can generate an OpenRoberta distribution.

When the docker image "gen" is run, it GENERATES an OpenRoberta distribution. It is NO OpenRoberta distribution by itself.
It gets version numbers independent from the OpenRoberta versions. During image creation a maven build is executed for
branch develop to fill the /root/.m2 cache. This makes later builds much faster.

```bash
REPO=/data/openroberta-lab/git/robertalab
cd $REPO/Docker
docker build -f meta/DockerfileGen_ubuntu_18_04 -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1
```

## define the variables used (set as needed!):

this is a setting usable on the test machine:

```bash
export HOME="/data/openroberta-lab
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

Note: when a container terminates, the message "... exited with code 130" is no error, but signals termination with CTRL-C
