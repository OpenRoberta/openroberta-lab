# Instructions to setup the lab using DOCKER container (2023-03-06)

This text is for *developer* and *maintainer* of the openroberta lab, *not* if you want to run a local server. Running a local server is described in our
[wiki on GitHub](https://github.com/OpenRoberta/openroberta-lab/wiki/Instructions-to-run-a-openroberta-lab-server-using-DOCKER). This text describes the
docker-related scripts, which are used for a large installation (as our prod and test servers are),

* to setup a docker network,
* create docker images for a data base server and lab server and
* run docker container of the OpenRoberta lab.

On every Linux-based machine it should be easy to run our setup. Commands are shown as bash commands. Windows 10 should work too, if you are careful with
pathes. Btw: if you install git for Windows, you get a nice bash running in Windows.

Docker must be installed. A lot of Internet resources describe how to proceed.

To create an instance of our docker installation,

* run `git clone https://github.com/OpenRoberta/openroberta-lab`
* in this directory run `./ora.sh new-docker-setup <BASEDIR>`. The `<BASEDIR>` must _not_ exist. Follow the instructions written by the command.
* create the servers you need by calling `./ora.sh new-server-in-docker-setup BASEDIR SERVER`. Follow the instructions written by the command. Note,
  that `test, dev, dev1...dev9` are the only names accepted as server names. It is easy to add or remove server later.
* for each server a data base for its own has to be created. Double check that the data bases are in `<BASEDIR>/db`.
* Put the database names into the global `<BASEDIR>/decl.sh`.

From now use the administration script `<BASEDIR>/scripts/run.sh` (abbreviated as `RUN`). Execute `RUN` without parameters to get a message explaining all
commands available.

The repo, from which you have run the `./ora.sh new-docker-setup <BASEDIR>` command is not needed anymore. You can reuse it by moving it into `<BASEDIR>/git`
instead of cloning it again, see below.

You have to

* clone the openroberta-lab repository into `<BASEDIR>/git`, update the server's `decl.sh appropriately` (set the repo, select the branch, ...)
* create a new docker bridge network (`RUN gen-net`). The name of the network is defined in the global `decl.sh`.
* generate a database server image and start the database container (`RUN gen-dbc` and `RUN start-dbc`). Check the result with `docker ps`. Inspect the log file
  in `BASEDIR/db/dbAdmin/`
* deploy the server ((`RUN deploy SERVER`). Check the result with `docker ps`. Inspect the log file in `BASEDIR/server/SERVER/admin/logging/...`

Looks more complicated as it is :-). I tried to make all scripts as robust as possible. Please mail any problems, improvements, ideas to reinhard.budde at
iais.fraunhofer.de

> Below I describe, what the design rationale behind our Docker architecture is, and how we, the lab maintainers, create images, that are available at
> dockerhub and are used in our Docker architecture. If you are curious, whether it works, you can read and try it. Besides that there is no need for you
> to read further. The text is for our internal documentation.

# (re-)create the base image (done from time to time)

The docker "base" image is used as basis for further images. It contains all software needed by the lab to create binaries for all the robots, i.e.

* the crosscompiler binaries itself. They are installed by calling `apt`, `wget`, ... .
* header, libraries, scripts, ..., to be used together with the cross compiler.

This is done in two steps. Because the crosscompiler binaries dont't change often, an image `openroberta/ccbin-${ARCH}` is build first. From this image
the `openroberta/base-${ARCH}` image is derived. This occurs much more often. Both images have an independent version numbering.

### step 1: image with crosscompiler binaries (usually not needed, because the crosscompiler binaries are stable)

The image is available at dockerhub. Name: openroberta/ccbin-x64:<number>>. Use the highest number.

It has been created using the shell commands:

```bash
BASE_DIR=/data/openroberta-lab
ARCH=x64             # either x64 or arm32v7
CCBIN_VERSION=2

cd ${BASE_DIR}/conf/${ARCH}/1-cc-binaries
docker build --no-cache -t openroberta/ccbin-${ARCH}:${CCBIN_VERSION} .
docker push openroberta/ccbin-${ARCH}:${CCBIN_VERSION}
```

_NOTE:_ If `openroberta/ccbin-${ARCH}` is rebuild with new or updated crosscompiler binaries, its version number `CCBIN_VERSION` has to be increased. Do _not_
forget, to increase the version numer in the next section, too.

### step 2: image with crosscompiler resources (more often needed, because our add-ons, e.g. header files, libs, ... change more frequently)

The image is available at dockerhub. Name: openroberta/base-x64:<number>>. Use the highest number.

To create it, you need a clone of our GitHub repository `ora-cc-rsc`, whose path is set in the commands below (`CC_RESOURCES`). It contains the resource to be
copied into the Docker image. _Make sure, that the repository `ora-cc-rsc` is clean. Uncommitted data will be lost. The Docker image has been created using the
shell commands:

```bash
BASE_DIR=/data/openroberta-lab
ARCH=x64             # either x64 or arm32v7
CCBIN_VERSION=2      # this is needed in the dockerfile!
BASE_VERSION=35
CC_RESOURCES=/data/openroberta-lab/git/ora-cc-rsc
cd $CC_RESOURCES
if [ ! -d .git ]; then echo "this script only runs in a git directory - exit 12"; exit 12; fi

git fetch --all; git reset --hard; git clean -fd
git checkout master; git pull
git checkout tags/${BASE_VERSION}

mvn clean install    # necessary to create the update resources for ev3- and arduino-based systems
docker build --no-cache -t openroberta/base-${ARCH}:${BASE_VERSION} \
       --build-arg CCBIN_VERSION=${CCBIN_VERSION} \
       -f $BASE_DIR/conf/${ARCH}/2-cc-resources/Dockerfile .
docker push openroberta/base-${ARCH}:${BASE_VERSION}
# do this if you sure, that your tag is the LATEST (this one is used for the INTEGRATION TESTS on GitHub!)
docker tag openroberta/base-${ARCH}:${BASE_VERSION} openroberta/base-${ARCH}:latest
docker push openroberta/base-${ARCH}:latest
```

_Note:_ If the git repository `ora-cc-rsc` is changed, the `openroberta/base-${ARCH}` image and all images built upon it must be rebuilt. This is fast, but
don't forget it! The version of the `openroberta/base-${ARCH}` image (a simple number) should match a tag in the git repository `ora-cc-rsc`. This reminds you,
that the data from that tag is the data stored in the base image. The variable `BASE_VERSION` in all `decl.sh` files of all servers contains this number, and
you have to edit these files, if you change the version number. It is legal, to use different base versions for different servers.

### step 3: image for integration tests (on GitHub, most be done ALWAYS after step 2 has succeeded)

this image contains a populated maven cache to speed up the nightly runs

```bash
BASE_DIR=/data/openroberta-lab
ARCH=x64             # either x64 or arm32v7
CCBIN_VERSION=2      # this is needed in the dockerfile!
BASE_VERSION=35

cd ${BASE_DIR}/
git fetch --all; git reset --hard; git clean -fd
git checkout master; git pull
git checkout tags/${BASE_VERSION}

mvn clean install    # necessary to create the update resources for ev3- and arduino-based systems
docker build --no-cache -t openroberta/base-${ARCH}:${BASE_VERSION} \
       --build-arg CCBIN_VERSION=${CCBIN_VERSION} \
       -f $BASE_DIR/conf/${ARCH}/2-cc-resources/Dockerfile .
docker push openroberta/base-${ARCH}:${BASE_VERSION}
# do this if you sure, that your tag is the LATEST (this one is used for the INTEGRATION TESTS on GitHub!)
docker tag openroberta/base-${ARCH}:${BASE_VERSION} openroberta/base-${ARCH}:latest
docker push openroberta/base-${ARCH}:latest
```

# Operating Instructions for the Test and Prod Server

For developing the lab, we use a test server, which serves many different instances of the openroberta lab server. Our prod server runs the prod version of the
openroberta lab server. They are setup in the same way. The following text describes the test server setup (the prod setup is the same, but uses a server
named `master` only).

## Directory structure

The template for our framework is contained in directory `Docker/openroberta`. It contains the directories

* conf - contains an example of an nginx and an apache2 configuration and directories used to generate docker images for the db and the lab server

* scripts - contains shell scripts to administrate the framework. The main script is `run.sh`. Call it without parameters to get help. the directory `helper`
  contains scripts, that are sourced from `run.sh` and do the "real" work.

* git - here one or more git repos, used to generate the openroberta server instances, are stored. At least one git repo is needed, usually a clone
  from https://github.com/OpenRoberta/openroberta-lab.git . The git repositories are changed by scripts during image generation. They are _only_ for this
  purpose. _Never_ store valuable data (branches, ...) there!

* server - contains the servers. Each server has a name (one of master,test,dev,dev1...dev9), which is also the name of a directory. This directory stores all
  data to configure the server in file `decl.sh`. In the file `_server-template/decl.sh` each property is explained. Each server has an associated docker
  image (and usually a running container), whose name is essentially the server's name.
    * In the subdirectory `export` all artifacts making up the server are stored during server generation.
    * In subdirectory `admin` all logging data and the tutorials are stored.

* db - contains the databases. Each database has a name (one of master,test,dev,dev1...dev9) matching the name of the server who needs this data. The name of
  the database is the name of the directory, which contains all database files. All databases are served by one Hsqldb server instance. Directory `dbAdmin`
  stores logging data and database backups. There is one docker image `openroberta/db_server` and usually a running container, whose name is `db-server`.

## Overview

* All running openroberta instances are generated from a branch or a commit of a git repository.

* Each instance of the openroberta lab server is running in a docker container of its own. The name of the corresponding image is
  `openroberta/server_${SERVER_NAME}_${ARCH}:${TAG_VERSION}`. `SERVER_NAME` is one of master,test,dev,dev1...dev9, `ARCH` is almost always x64 and
  `TAG_VERSION` is almost always the value of `BASE_VERSION` from the server's `decl.sh`. There is only one exception: if the server's `decl.sh` contains a
  declaration of `TAG_VERSION`, then this value is used as tag number *when the server's container is started*.

* Each instance of the openroberta lab server is connected to a database dedicated to this openroberta lab server. All databases are published by one database
  server running in a docker container of its own.

For instance, if you want to deploy the `develop` branch on server `dev4`, do the following (the variables used are explained below):

* make sure, that in the directory `$SERVER_DIR/dev4` the file `decl.sh` contains the expected data (branch name, port number for accessing the server, e.g.)
* be sure, that in `$DATABASE_DIR/dev4` a valid database is available. Check whether the global `decl.sh` contains `dev4` in variable `DATABASES`. It not, add
  it and restart the database server by `$SCRIPT_DIR/run.sh start-dbc`

All container of an openroberta docker installation use a bridge network with (the unique!) name `$DOCKER_NETWORK_NAME`. Assuming that the network is created,
the database container is running, then you may (re-)deploy `dev4` by executing

```bash
$SCRIPT_DIR/run.sh deploy dev4 # 'deploy dev4' is a shorthand for 'gen dev4' and 'start dev4'
docker ps                      # the container for 'dev4' should be running
```

## Web server software

We need a running web server to distribute requests to the different openroberta lab servers. This is done with web server software. Apache and nginx are most
popular. Inside the server docker container the port 1999 is used. When a server container is started, this port is mapped to a accessible port on the host
machine (this maybe 1999 again, or 1998, ... or any other port. For instance, on the prod server

* `container:1999` is mapped to
* `host:8080` and
* the web server maps that to the offical addresses `lab.open-roberta.org.443` and `lab.open-roberta.org.80`

The nginx configuration examples are in the directory `z-nginx`. The configuration directory on the host machine is `/etc/nginx/`. In the example
file `nginx.conf` see the ssl-, -server and include-section in the example file for the global configuration. The example file `lab.open-roberta`
contains the open-roberta specific configuration. The Apache2 configuration examples are in the directory `z-apache2`.

Actually we are using nginx. Apache would do as well.

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

The ports `1999` to `1989` are used by up to 11 openroberta server. Each of these servers is based on an embedded jetty. The names of the servers are (b.t.w.:
this matches the virtual host names of the web server configuration):

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
* dev1 up to dev9: images are `openroberta/server__dev1:$BASE_VERSION` to `openroberta/server_dev9:$BASE_VERSION` and the running container have names `dev1`
  to `dev9`

Generating the images is done by using the data from `decl.sh` from the server directory. Generation will:

* pull actual data into a Git repo as declared and checkout the branch/commit as declared
* run `mvn clean install -DskipTests`
* export the server into the export directory
* create the docker image from that directory

Note: by using `flock` the generation of images is done sequentially. A new request for image generation is blocked as long as another generation is running. So
it is safe, that many test server share one git repository. This increases the performance of server generation a lot.

## database server

each server needs connection to a Hsqldb-based database. The database URI's match the openroberta lab server names:

* `jdbc:hsqldb:hsql://localhost/openroberta-db-test`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev1` to `jdbc:hsqldb:hsql://localhost/openroberta-db-dev9`

These databases are served by one database server. The database files are located at (and must be supplied by YOU!):

* `$DATABASE_DIR/test`
* `$DATABASE_DIR/dev`
* `$DATABASE_DIR/dev1` to `$BASE/db/dev9`

The database server is deployed in a docker container with name `ora-db-server` created from the docker image `rbudde/openroberta--db-server:<hsqldb-version>`.
The database server is listening to the port `$DATABASE_SERVER_PORT` as defined in the global `decl.sh`. This is usually the Hsqldb default `9001`.

## Automatic deploy, database backup, removing temporary files, automatic restart

The shell script `$SCRIPT_DIR/run.sh` has commands, that are used for operating purposes.

* `auto-deploy`: usually called from cron. It takes server names from the variable `AUTODEPLOY` from the glocal `decl.sh` and re-deploys each server, if the git
  repository connected to this server has got new commits. Use `crontab -e` to add the following line to the crontab to look for commits every 5 minutes:

```bash
*/5 * * * * bash <SCRIPT_DIR>/scripts/run.sh -q auto-deploy >><BASE_DIR>/logs/cronlog.txt
```

* `backup`: usually called from cron. It takes a database name and creates a database backup in the `dbAdmin` directory. Use `crontab -e` to add the following
  line to the crontab to generate a database backup every night at two o'clock:

```bash
0 2 * * * bash <SCRIPT_DIR>/run.sh -q backup <database-name> >><BASE_DIR>/logs/cronlog.txt

```

* `cleanup-temp-user-dirs`: usually called from cron. It takes a server name and runs a shell in the corresponding container, that will remove temporary old
  data allocated by the cross compiler. It is assumed, that these crosscompiler-allocated files are used not longer than one day after their creation.
  Use `crontab -e` to add the following line to the crontab to remove garbage every night 20 minutes after two o'clock:

```bash
20 2 * * * bash <SCRIPT_DIR>/run.sh -q admin <server-name> cleanup-temp-user-dirs >><BASE_DIR>/logs/cronlog.txt
```

* `auto-restart`: this command spawns a process, that will not terminate by itself. It checks the availablity of the server (using the server-url)
  and, if the server is not available when tried to access twice, it will restart the server. Logging info is written to `<BASE_DIR>/logs/autorestart.txt`. the
  pid of the auto-restart process is logged. The process may be terminated at any time. The process can be initiated explicitly by executing:

```bash
bash <SCRIPT_DIR>/run.sh -q auto-restart <server-name> <server-url>
```

The command used for initiating 'auto-restart' for the prod server and looking up the pid (if not found in the log-file) are:

```bash
/bin/bash /data/openroberta-lab/scripts/run.sh -q auto-restart master http://me-roblab-prod:8080
ps -AfL | fgrep auto-restart
```

* `alive`: usually called from cron. It takes a server URL and checks whether the server is alive. It sends by default mail to admins. Use `crontab -e` to add
  the following line to the crontab. call <SCRIPT_DIR>/run.sh -help to learn about other parameters of the call:

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

This feature is needed to protect against data loss if a server crashes. As the database contains user data, the safety requirements for the machine to which
the backups are copied, must be at least the safety requirements for the machine, that runs the database server. Ssh keys are used, thus a setup on both servers
is needed. The two servers affected are called "SRC-SERVER" and "TARGET-SERVER".

* On the TARGET-SERVER: check whether `ssh <anyExistingUser@TARGET-SERVER` succeeds.
* On the TARGET-SERVER: `adduser dbBackup --force-badname`. The user's password has to be strong.
* On the TARGET-SERVER: `cd <BASE_DIR_ON_TGT_S>/db/dbAdmin; mkdir -p dbBackupSave/<db-name>`
  `chgrp dbBackup dbBackupSave dbBackupSave/<db-name>; chmod -R g+rwx dbBackup`.
* On the TARGET-SERVER: `su dbBackup; ssh-keygen -t rsa -b 4096`.
* On the SRC-SERVER:    `adduser dbBackup --force-badname`. The user's password has to be strong.
* On the SRC-SERVER:    `cd <BASE_DIR_ON_SRC_S>/db/dbAdmin; chgrp -R dbBackup dbBackup; chmod -R g+rwx dbBackup`
* On the TARGET-SERVER: `ssh-copy-id -i /home/dbBackup/.ssh/id_rsa.pub dbBackup@<SRC-SERVER>; ssh dbBackup@<SRC-SERVER> # test for success`.

How does it work?

* On SRC-SERVER (for instance every day at 2:00 AM started by a cronjob) a database backup is generated. See the description above, how to achieve that. The
  backup is readable by all members of group `dbBackup`, to which user `dbBackup` belongs.
* On TARGET-SERVER (for instance every day at 3:00 AM started by a cronjob) the database backup is saved to protect us against data loss. This works, because
  user `dbBackup` on TARGET-SERVER can use `scp` for user `dbBackup` on SRC-SERVER and the ssh key installed above allows that. The rights on SRC-SERVER are
  restricted to the rights of user `dbBackup` on SRC-SERVER. Essentially this is the access to the backup directory.
  `
  The cronjob on TARGET-SERVER could look like (note, that the script runs as user `dbBackup`, and, that cron expects the command in one line).

```bash
0 3 * * * /usr/bin/sudo -u dbBackup <SCRIPT_DIR_ON_TGT_S>/run.sh -q backup-save
          dbBackup@<SRC-SERVER>:<BASE_DIR_ON_SRC_S>/db/dbAdmin/dbBackup/<db-name> db/dbAdmin/dbBackupSave/<db-name>
          >><BASE_DIR>/logs/cronlog.txt 2>&1
```

## Note about the -d command line arguments for the openrobertalab server container

The global properties needed for the openrobertalab server are found in resource `/openroberta.properties`. At start time these parameter can be modified by an
arbitrary number of command line arguments like `-d KEY=VALUE`. The final list of these `-d` arguments is build as follows:

* script `start.sh`: the shell script `start.sh` is inside the openrobertalab server container and starts the JVM with main class `ServerStarter`. It contains
  some `-d` arguments, that are the same for _all_ openrobertalab server container and adds more `-d` arguments (passed by the script that starts the container)
  by a trailing `$*`
* script `_start.sh`: the docker container is started (via the main script `run.sh`) by the bash statements found in `_start.sh`. Here more `-d` statements are
  added, that refer to the OpenRoberta server instance (master, test, dev). They add database names, network names, logging level, logging configuration and
  mount points.
* variable `START_ARGS`: every OpenRoberta server has a configuration file `decl.sh`. Here the shell variable `START_ARGS` can define more `-d` arguments.
  *NOTE:* this is the place for the last deployer-defined additions: declare the list of robot plugins to use, whether this is the public server or not, and so
  on. It is possible, but _not_ adviced to overwrite properties already defined at the two other places described above



