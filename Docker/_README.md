# Instructions to setup and run the test and prod server using DOCKER container (2020-06-08)

This text describes the docker-related scripts, that are used to setup a docker network, create docker images and run docker container of the OpenRoberta lab. Exactly these
scripts are used to run OpenRoberta's prod and test servers. On every Linux-based machine it should be easy to run our setup. To create an instance of our docker installation,
clone the openroberta-lab git and run `./ora.sh new-docker-setup <BASEDIR_MUST_NOT_EXIST>`. Follow the instructions
from the script. From this point use the the administration script `<BASEDIR>/scripts/run.sh` (abbreviated as `RUN`). Execute `RUN` without parameters to get a message
explaining all commands available. You have to

* clone the openroberta-lab repository into `<BASEDIR>/git`, update the server's `decl.sh appropriately` (set the repo, select the branch, ...)
* create a new docker bridge network (`RUN gen-net`). The name is defined in the global `decl.sh`.
* create the databases needed in directory `db`. Server names are test, dev, dev1...dev9 and must not exist. For each server one database with exactly the server's name is needed.
  You can create empty databases by calling `./admin.sh -dbParentdir <non-existing-directory-of-your-choice> create-empty-database` after a `mvn clean install -DskipTests`
  and copy them to the desired locations. Put the database names into the global `decl.sh`.
* generate a database server image and start the database container (`RUN gen-dbc` and `RUN start-dbc`). Check the result with `docker ps`. Inspect the log file in `BASEDIR/db/dbAdmin/`
* create the servers for which you have databases (see above) by calling `./ora.sh new-server-in-docker-setup BASEDIR SERVER`. For each server in `BASEDIR/server/SERVER`
  update the `decl.sh (set the repo, select the branch, ...). Note, that `test, dev, dev1...dev9` are the only names accepted as server names.
* deploy the server ((`RUN deploy SERVER`). Check the result with `docker ps`. Inspect the log file in `BASEDIR/server/SERVER/admin/logging/...`

Looks more complicated as it is :-). Details of the file system structure used and the more functionality supported (database backup, alice checking, autorestart) are described later.
I tried to make all scripts as robust as possible. Please mail any problems, improvements, ideas to reinhard.budde at iais.fraunhofer.de

We generate docker images for different architectures. Currently we support

* `x64` - the standard architecture. Our prod server, your laptop, ... use this architecture
* `arm32v7` - the architecture used by Raspberry pi's 3 and 4, for example. These "small" devices can run a docker demon, a database container and a
  jetty-based rest-server without performance problems. We support these devices to run local servers: for privacy reasons, bad iternet connectivity, ... .
  
In the following the shell variable `ARCH` refers to either `x64` or `arm32v7`. The architecture is auto-detected by the `RUN` script.

Docker must be installed. Google for it, usually the job is done by executing

```bash
curl -sSL get.docker.com | sh
```

# (re-)create the base image (done from time to time)

## generate the "base" IMAGE. This image contains the crosscompiler and the crosscompiler resources (header files, ...).

The docker "base" image is used as basis for further images. It contains all software needed by the lab to create binaries for all the robots, i.e.

* the crosscompiler binaries itself. They are installed by calling `apt`, `wget`, ... .
* header etc. to be used together with the cross compiler. They are copied from our git repository `ora-cc-rsc`.

This done in two steps. Because the crosscompiler binaries dont't change often, an image `openroberta/ccbin-${ARCH}` is build first. From this image
the `openroberta/base-${ARCH}` image is derived. This occurs much more often. Both images have an independant version numbering.

### step 1: image with crosscompiler binaries (usually not needed, because the crosscompiler binaries are stable)

```bash
BASE_DIR=/data/openroberta-lab
ARCH=x64 # either x64 or arm32v7
CCBIN_VERSION=1

cd ${BASE_DIR}/conf/${ARCH}/docker-for-meta-1-cc-binaries
docker build --no-cache -t openroberta/ccbin-${ARCH}:${CCBIN_VERSION} .
docker push openroberta/ccbin-${ARCH}:${CCBIN_VERSION}
```

_NOTE:_ If `openroberta/ccbin-${ARCH}` is rebuild with new or updated crosscompiler binaries, its version number `CCBIN_VERSION` has to be increased.
Do _not_ forget, to increase the version numer in the next section, too.

### step 2: image with crosscompiler resources (more often needed, because our add-ons, e.g. header files, libs, ... change more frequently)

```bash
BASE_DIR=/data/openroberta-lab
ARCH=x64 # either x64 or arm32v7
CCBIN_VERSION=1 # this is needed in the dockerfile!
BASE_VERSION=25
CC_RESOURCES=/data/openroberta-lab/git/ora-cc-rsc
cd $CC_RESOURCES

git checkout develop; git pull; git checkout master; git pull
git checkout tags/${BASE_VERSION}

mvn clean install # necessary to create the update resources for ev3- and arduino-based systems
docker build --no-cache -t openroberta/base-${ARCH}:${BASE_VERSION} \
       --build-arg CCBIN_VERSION=${CCBIN_VERSION} \
       -f $BASE_DIR/conf/${ARCH}/docker-for-meta-2-cc-resources/Dockerfile .
docker push openroberta/base-${ARCH}:${BASE_VERSION}
```

_Note:_ If the git repository `ora-cc-rsc` is changed, the `openroberta/base-${ARCH}` image and all images built upon it must be rebuilt. This is fast,
but better do not forget! The version of the `openroberta/base-${ARCH}` image (a simple number) should match a tag in the git repository `ora-cc-rsc`.
This reminds you, that the data from that tag is the data stored in the base image. The variable `BASE_VERSION` contains this number, which is both a tag name
in git and a version number in docker.

### step 3 image for the integration tests (x64 only)

It would be easy to build this image for the `arm32v7` architecture. But our `bamboo` server used for automated integration tests run on `x64` machines.
Thus there is no need for this image. This may change in the future.

This step creates an image, built upon the "base" image, that has executed a git clone of the
main git repository `openroberta-lab` and has executed a `mvn clean install`. This is done to fill the
(mvn) cache and speeds up later builds considerably. The entry point is defined as the bash script "runIT.sh".
If called, it will checkout a branch given as parameter and runs both the tests and the integration tests.

```bash
BASE_DIR=/data/openroberta-lab
ARCH=x64
BASE_VERSION=25
BRANCH=develop
cd ${BASE_DIR}/conf/${ARCH}/docker-for-test
docker build --no-cache --build-arg BASE_VERSION=${BASE_VERSION} --build-arg BRANCH=${BRANCH} \
       -t openroberta/it-${ARCH}:${BASE_VERSION} .
docker push openroberta/it-${ARCH}:${BASE_VERSION}
```

To run the integration tests on your local machine (usually a build server like `bamboo` will do this), execute:

```bash
BASE_VERSION=25
export BRANCH='develop'
docker run openroberta/it-${ARCH}:${BASE_VERSION} ${BRANCH} x.x.x # x.x.x is the db version and unused for tests
```

# Operating Instructions for the Test and Prod Server

Our test server serves many different instances of the openroberta lab server. Our prod server runs the prod version of the openroberta lab server.
They are setup in the same way. The following text describes the test server setup (the prod setup is the same, but uses the server named `master`).

## Directory structure

The template for this framework is contained in directory `Docker/openroberta`. It contains the directories

* conf - contains an example of an nginx and an apache2 configuration and four directories used to generate docker images for the db and the jetty server
* scripts - contains shell scripts to administrate the framework. The main script is `run.sh`. Call it without parameters to get help.
  the directory `helper` contains scripts, that are sourced from `run.sh` and do the "real" work.
* git - here one or more git repos, used to generate the openroberta server instances, are contained. At least one git repo is needed, usually a clone
  from https://github.com/OpenRoberta/openroberta-lab.git
* server - contains the servers. Each server has a name (one of master,test,dev,dev1...dev9), which is also the name of a directory. This directory stores all
  data to configure the server in file `decl.sh`. In directory `export` all artifacts making up the server are stored during server
  generation. In directory `admin` all logging data and the tutorials are stored.
  Each server has an associated docker image (and usually a running container), whose name is essentially the server's name.
* db - contains the databases. Each database has a name (one of master,test,dev,dev1...dev9) matching the name of the jetty server who needs this data.
  the name of the database is the name of a directory, which in turn contains all database files. All databases are served by one Hsqldb server instance.
  Directory `dbAdmin` stores logging data and database backups.
  There is one docker image `openroberta/db_server` and usually a running container, whose name is usually `db-server`.

## Overview

All running openroberta instances are generated from a branch or a commit of a git repository.

* Each instance of the openroberta lab server is running in a docker container of its own.
* Each instance of the openroberta lab server is connected to a database dedicated to this openroberta lab server. All databases are published by one
  database server running in a docker container of its own.

For instance, if you want to deploy the `develop` branch on server `dev4`, do the following (the variables used are explained below):

* make sure, that in the directory `$SERVER_DIR/dev4` the file `decl.sh` contains the expected data (branch name, port number for accessing the server, e.g.)
* be sure, that in `$DATABASE_DIR/dev4` a valid database is available. Check whether `decl.sh` contains `dev4` in variable `DATABASES`. It not,
  add it and restart the database server by `$SCRIPT_DIR/run.sh start-dbc`

All container of an openroberta docker installation use a bridge network with (the unique!) name `$DOCKER_NETWORK_NAME`.
Assuming that the network is created, the database container is running, then you may (re-)deploy `dev4` by executing

```bash
$SCRIPT_DIR/run.sh deploy dev4 # 'deploy dev4' is a shorthand for 'gen dev4' and 'start dev4'
docker ps                      # the container for 'dev4' should be running
```

## Web server software

We need a running web server to distribute requests to the different openroberta lab servers. This is done with web server software. Apache and nginx are the
most popular ones at the moment. Inside the server docker container the port 1999 is used. When a server container is started, this port is mapped to a
accessible port on the host machine (this maybe 1999 again, or 1998, ... or any other port. For instance, on the prod server

* `container:1999` is mapped to
* `host:8080` and
* the web server maps that to the offical addresses `lab.open-roberta.org.443` and `lab.open-roberta.org.80`

The nginx configuration examples are in the directory `z-nginx`. The configuration directory on the host machine is `/etc/nginx/`.
In the example file `nginx.conf` see the ssl-, -server and include-section in the example file for the global configuration. The example file `lab.open-roberta`
contains the open-roberta specific configuration. The Apache2 configuration examples are in the directory `z-apache2`.

Actually we are using nginx. Apache would do it as well.

## Conventions
All data is stored relative to a base directory `$BASE_DIR` (your choice, we use `/data/openroberta-lab`). Abbreviations:

```bash
BASE_DIR=/data/openroberta-lab # the directory, in which the main configuration file 'decl.sh' is foundd
CONF_DIR=$BASE_DIR/conf
SCRIPT_DIR=$BASE_DIR/scripts
SERVER_DIR=$BASE_DIR/server
DATABASE_DIR=$BASE_DIR/db
```

## Description of a typical setup of a docker installation for many openroberta lab server

The ports `1999` to `1989` are used by up to 11 openroberta server. Each of these servers is based on an embedded jetty.
The names of the servers are (b.t.w.: this matches the virtual host names of the web server configuration):

* test: configuration is in `$SERVER_DIR/test/decl.sh` (usually port 1999)
* dev: configuration is in `$SERVER_DIR/dev/decl.sh` (usually port 1998)
* dev1: configuration is in `$SERVER_DIR/dev1/decl.sh` (usually port 1997)
* dev2: configuration is in `$SERVER_DIR/dev2/decl.sh` (usually port 1996)
* dev3: configuration is in `$SERVER_DIR/dev3/decl.sh` (usually port 1995)
* dev4: configuration is in `$SERVER_DIR/dev4/decl.sh` (usually port 1994)
* dev5: configuration is in `$SERVER_DIR/dev5/decl.sh` (usually port 1993)
* dev6: configuration is in `$SERVER_DIR/dev6/decl.sh` (usually port 1992)
* dev7: configuration is in `$SERVER_DIR/dev7/decl.sh` (usually port 1991)
* dev8: configuration is in `$SERVER_DIR/dev8/decl.sh` (usually port 1990)
* dev9: configuration is in `$SERVER_DIR/dev9/decl.sh` (usually port 1989)

The name of the docker images and the name of the running containers are:

* test: image is `openroberta/server_test:$BASE_VERSION` and the running container has name `test`
* dev: image is `openroberta/server_dev:$BASE_VERSION` and the running container has name `dev`
* dev1 up to dev9: images are `openroberta/server__dev1:$BASE_VERSION` to `openroberta/server_dev9:$BASE_VERSION` and
  the running container have names `dev1` to `dev9`

Generating the images is done by using the data from `decl.sh` from the server directory. Generation will:

* pull actual data into a Git repo as declared and checkout the branch/commit as declared
* run `mvn clean install -DskipTests`
* export the server into the export directory
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

The database server is deployed in a docker container with name `ora-db-server` created from the docker image `rbudde/openroberta--db-server:2.4.0`.
The database server is listening to the port `$DATABASE_SERVER_PORT` as defined in the global `decl.sh`. This is often the
Hsqldb default `9001`.

## Automatic deploy, database backup, removing temporary files, automatic restart

The shell script `$SCRIPT_DIR/run.sh` has commands, that are used for operating purposes.
 
* `auto-deploy`: usually called from cron. It takes server names from the variable `AUTODEPLOY` from the glocal `decl.sh` and re-deploys each server, if the git
  repository connected to this server has got new commits. Use `crontab -e` to add the following line to the crontab to look for commits every 5 minutes:
  
```bash
*/5 * * * * bash <SCRIPT_DIR>/scripts/run.sh -q auto-deploy >><BASE_DIR>/logs/cronlog.txt
```

* `backup`: usually called from cron. It takes a database name and creates a database backup in the `dbAdmin` directory.
  Use `crontab -e` to add the following line to the crontab to generate a database backup every night at two o'clock:
  
```bash
0 2 * * * bash <SCRIPT_DIR>/run.sh -q backup <database-name> >><BASE_DIR>/logs/cronlog.txt

```

* `cleanup-temp-user-dirs`: usually called from cron. It takes a server name and runs a shell in the corresponding container, that will remove temporary
  old data allocated by the cross compiler. It is assumed, that these crosscompiler-allocated files are used not longer than one day after their creation.
  Use `crontab -e` to add the following line to the crontab to remove garbage every night 20 minutes after two o'clock:
  
```bash
20 2 * * * bash <SCRIPT_DIR>/run.sh -q admin <server-name> cleanup-temp-user-dirs >><BASE_DIR>/logs/cronlog.txt
```

* `auto-restart`: this command spawns a process, that will not terminate by itself. It checks the availablity of the server (using the server-url)
  and, if the server is not available when tried to access twice, it will restart the server. Logging info is written to `<BASE_DIR>/logs/autorestart.txt`.
  the pid of the auto-restart process is logged. The process may be terminated at any time. The process can be initiated explicitly by executing:
  
```bash
bash <SCRIPT_DIR>/run.sh -q auto-restart <server-name> <server-url>
```
The command used for initiating 'auto-restart' for the prod server and looking up the pid (if not found in the log-file) are:
```bash
/bin/bash /data/openroberta-lab/scripts/run.sh -q auto-restart master http://me-roblab-prod:8080
ps -AfL | fgrep auto-restart
```

* `alive`: usually called from cron. It takes a server URL and checks whether the server is alive. It sends by default mail to admins.
  Use `crontab -e` to add the following line to the crontab. call <SCRIPT_DIR>/run.sh -help to learn about other parameters of the call:
  
```bash
20 2 * * * bash <SCRIPT_DIR>/run.sh -q alive <https://server-url> >><BASE_DIR>/logs/cronlog.txt
```

## Init scripts

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
# Description:       Start, stop and restart the database server and the openroberta server
### END INIT INFO

# Author: rbudde

BASE_DIR=<BASE_DIR>
SCRIPTS=$BASE_DIR/scripts
case "$1" in
    start)   bash $SCRIPTS/run.sh -q -yes start-all                        >>$BASE_DIR/logs/init.txt
             ;;
    stop)    bash $SCRIPTS/run.sh -q -yes stop-all                         >>$BASE_DIR/logs/init.txt
             ;;
    restart) bash $SCRIPTS/run.sh -q -yes stop-all                         >>$BASE_DIR/logs/init.txt
             bash $SCRIPTS/run.sh -q -yes start-all                        >>$BASE_DIR/logs/init.txt
             ;;
    *)       echo "invalid command \"$1\". Usage: $0 {start|stop|restart}" >>$BASE_DIR/logs/init.txt
             exit 12
             ;;
esac
```

The `-yes` effects, that all security questions, that are asked in interactive mode, are answered with `y`.

After putting `openrobertalab` into `/etc/init.d`, the service is added with the commands:

```bash
chmod ugo+x /etc/init.d/openrobertalab

systemctl enable openrobertalab
systemctl start openrobertalab

# systemctl daemon-reload                 # is the RELOAD really needed? reload all config files and (re-)start all services
systemctl status openrobertalab           # to see logging
journalctl -u openrobertalab.service      # to see more logging
```

## Saving the database backups from a server to another server

This feature is needed to protect against data loss if a server crashes. As the database contains user data, the safety requirements for the machine to
which the backups are copied, must be at least the safety requirements for the machine, that runs the database server. Ssh keys are used, thus a setup
on both servers is needed. The two servers affected are called "SRC-SERVER" and "TARGET-SERVER".

* On the TARGET-SERVER: check whether `ssh <anyExistingUser@TARGET-SERVER` succeeds.
* On the TARGET-SERVER: `adduser dbBackup --force-badname`. The user's password has to be strong.
* On the TARGET-SERVER: `cd <BASE_DIR_ON_TGT_S>/db/dbAdmin; mkdir -p dbBackupSave/<db-name>`
                        `chgrp dbBackup dbBackupSave dbBackupSave/<db-name>; chmod -R g+rwx dbBackup`.
* On the TARGET-SERVER: `su dbBackup; ssh-keygen -t rsa -b 4096`.
* On the SRC-SERVER:    `adduser dbBackup --force-badname`. The user's password has to be strong.
* On the SRC-SERVER:    `cd <BASE_DIR_ON_SRC_S>/db/dbAdmin; chgrp -R dbBackup dbBackup; chmod -R g+rwx dbBackup`
* On the TARGET-SERVER: `ssh-copy-id -i /home/dbBackup/.ssh/id_rsa.pub dbBackup@<SRC-SERVER>; ssh dbBackup@<SRC-SERVER> # test for success`.

How does it work?

* On SRC-SERVER (for instance every day at 2:00 AM started by a cronjob) a database backup is generated. See the description above, how to achieve that.
  The backup is readable by all members of group `dbBackup`, to which user `dbBackup` belongs.
* On TARGET-SERVER (for instance every day at 3:00 AM started by a cronjob) the database backup is saved to protect us against data loss. This works, because
  user `dbBackup` on TARGET-SERVER can use `scp` for user `dbBackup` on SRC-SERVER and the ssh key installed above allows that. The rights on SRC-SERVER are restricted
  to the rights of user `dbBackup` on SRC-SERVER. Essentially this is the access to the backup directory.
`
The cronjob on TARGET-SERVER could look like (note, that the script runs as user `dbBackup`, and, that cron expects the command in one line).

```bash
0 3 * * * /usr/bin/sudo -u dbBackup <SCRIPT_DIR_ON_TGT_S>/run.sh -q backup-save
          dbBackup@<SRC-SERVER>:<BASE_DIR_ON_SRC_S>/db/dbAdmin/dbBackup/<db-name> db/dbAdmin/dbBackupSave/<db-name>
          >><BASE_DIR>/logs/cronlog.txt 2>&1
```

## Note about the -d command line arguments for the openrobertalab server container

The global properties needed for the openrobertalab server are found in resource `/openroberta.properties`. At start time these parameter can be modified
by an arbitrary number of command line arguments like `-d KEY=VALUE`. The final list of these `-d` arguments is build as follows:

* script `start.sh`: the shell script `start.sh` is inside the openrobertalab server container and starts the JVM with main class `ServerStarter`.
  It contains some `-d` arguments, that are the same for _all_ openrobertalab server container and adds more `-d` arguments (passed by the script that starts
  the container) by a trailing `$*`
* script `_start.sh`: the docker container is started (via the main script `run.sh`) by the bash statements found in `_start.sh`. Here more `-d` statements are added,
  that refer to the OpenRoberta server instance (master, test, dev). They add database names, network names, logging level, logging configuration and mount points.
* variable `START_ARGS`: every OpenRoberta server has a configuration file `decl.sh`. Here the shell variable `START_ARGS` can define more `-d` arguments.
  *NOTE:* this is the place for the last deployer-defined additions: declare the list of robot plugins to use, whether this is the public server or not, and so on.
  It is possible, but _not_ adviced to overwrite properties already defined at the two other places described above
  
## Can be ignored, TODO: to be integrated somewhere else

> Since the fix needed to compile c4ev3 programs on raspberry hasn't been published yet, the Dockerfile
> contains 3 lines that references 3 folders/files that need to be in this folder before creating the image.
> The files are:
> - recent version of ora-cc-rsc
> - recent version of RobotEV3.jar
> - C4EV3.Toolchain-2019.08.0-rpi.tar.gz


