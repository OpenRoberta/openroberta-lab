# Operating the test server (2019-03-06 17:00:00)

The test server serves many different instances of the openroberta lab server. These instances are generated from a branch or a commit of (usually) one or
many git repositories. Each instance of the openroberta lab server is running in a docker container of its own. Each instance of the openroberta lab server
is connected to a database dedicated to this openroberta lab server. All databases are published by one database server running in a docker container of its own.

If you want to deploy the `develop` branch on server `dev4`, for instance, do the following (the variables used are explained below):

* modify in the directory `$SERVER/dev4` the file `decl.sh` as you need it (set the branch name, e.g.)
* be sure, that in `$BASE/db/dev4` a valid database is available. Check whether `$BASE/db/databases.txt` contains `dev4`. It not, add it and restart
  the database server by `$CONF/run.sh startDbC`

Assuming that the network `ora-net` is created, the database container is running, then you (re-)deploy `dev4` and view the system state by executing

```bash
$CONF/run.sh deploy dev4 # shorthand for gen and start, e.g. the same as the 2 lines below

$CONF/run.sh gen dev1
$CONF/run.sh start dev1

$CONF/run.sh info        # show the state of all test server
```

## Apache2 configuration

We need a running web server to distribute requests to the different openroberta lab servers. This is done with Apache (nginx would be fine, too).
Configuration examples exist in the directory apache2:

* `localhost:1999` is bound to `test.open-roberta.org.443` and `test.open-roberta.org.80`
* `localhost:1998` is bound to `dev.open-roberta.org.443`  and `dev.open-roberta.org.80`
* `localhost:1997` is bound to `dev1.open-roberta.org.443` and `dev1.open-roberta.org.80`

and so on. B.t.w.: the ports are not fixed. But they must be consistent between apache2 and lab server.

## General structure

All data is stored relative to the directory `/data/openroberta`. We use the following abbreviations:

```bash
BASE=/data/openroberta
CONF=$BASE/conf
SERVER=$BASE/server
```

## The openroberta lab servers

The ports `1999` to `1989` are used by up to 11 openroberta lab server. Each of these servers is based on an embedded jetty.
The names of the servers are (b.t.w.: this matches the virtual host names of the apache configuration):

* test: configuration is in `$SERVER/test/decl.sh`
* dev: configuration is in `$SERVER/dev/decl.sh`
* dev1 up to dev9: configuration is in `$SERVER/dev1/decl.sh` up to `$SERVER/dev9/decl.sh`

See `$SERVER/dev1` for example. The name of the docker images and the name of the running containers are:

* test: image is `rbudde/openroberta_lab_test:1` and container has name `test`
* dev: image is `rbudde/openroberta_lab_dev:1` and container has name `dev`
* dev1 up to dev9: images are `rbudde/openroberta_lab_dev1:1` to `rbudde/openroberta_lab_dev9:1` and container have names `dev1` to `dev9`

Generating the images is done by using the data from `decl.sh`:

* pulling actual data into a Git repo and checking out the branch/commit as declared (you can suppress this with `GIT_UPTODATE=true`)
* running `mvn clean install -DskipTests`
* exporting the server into an export directory
* creating the docker image from that directory

Note: by using `flock` the generation of images is done sequentially. A new request for image generation is blocked as long as another
generation is running. Thus it is safe, that many test server share a git repository. This increases the performance of server generation a lot.

## database server

each jetty server needs connection to a Hsqldb-based database. The database URI's match the openroberta lab server names:

* `jdbc:hsqldb:hsql://localhost/openroberta-db-test`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev`
* `jdbc:hsqldb:hsql://localhost/openroberta-db-dev1` to `jdbc:hsqldb:hsql://localhost/openroberta-db-dev9`

These databases are served by one database server. The database files are located at (and must be supplied by YOU!):

* `$BASE/db/test`
* `$BASE/db/dev`
* `$BASE/db/dev1` to `$BASE/db/dev9`

The database server is deployed in a docker container with name `ora-db-server` created from the docker image `rbudde/openroberta_db:2.4.0`.
The database server is listening to port `9001`. This port must be mapped to `9001` of the base machine.

## Scripting

Everything is done with the help of the shell script `$CONF/scripts/run.sh`. I tried to make this scripts as robust as possible. Please send any
problems, improvements, ideas to reinhard.budde at iais.fraunhofer.decl

The main commands of the script are (run `$CONF/scripts/run.sh help` to see the names) the following. Note, that if the server name is missing,
_all_ servers found in file `$SERVER/servers.txt` are taken into account:

* `gen <name>`: generate the docker image for server <name>. Use the configuration found in `$SERVER/<name>`.
* `start [<name>]`: start a container with the image generated for server <name>. Before doing that, stop a container running sever <name>
* `stop [<name>]`: stop a container for server <name>. Remove the exited container.
* `deploy [<name>]`: shortcut for gen and start for server <name>.

Rarely used are commands:

* `genDbC`: generate the database container `rbudde/openroberta_db:2.4.0`. Used once when the test server is setup.
* `startDbC`: (re-)start the database container servicing the databases, whose names are found in file `BASE/db/databases.txt`
* `stopDbC`: stop the database container
* `prune`: remove all stale data from the docker installation. IMPORTANT if you are low with disc space.

Getting information:

* `info`: show images and running containers
* `network`: show the ora-net network
* `logs`: show the last 10 lines of all logs of all running containers. B.t.w.: `docker logs -f <name>` is convenient to see how a server works.
* of course, docker commands can help, too: `docker logs -f dev1` or `docker ps`

## Init scripts and automatic deploy

The shell script `$CONF/scripts/run.sh` has two more commands, that are used to initialize the test setup (on server restart, for example) and to
deploy one or more servers automatically if new commits hit the remote repo.
 
* `autoDeploy`: usually called from cron. Reads the server names from file `$SERVER/autodeploy.txt` and re-deploys each server, if the git
  repository connected to this server has got new commits. A typical line in crontab to run this is:
  
```bash
*/5 * * * * bash /data/openroberta/conf/scripts/run.sh -q autoDeploy >>/data/openroberta/logs/cronlog.txt
```

* `startAll` and `stopAll`: usually called from the configuration file `robertalab.sh` found in `/etc/init.d`. A typical script is:

```bash
#!/bin/sh
### BEGIN INIT INFO
# Provides:          robertalab
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: the openroberta service
# Description:       Start, stop and restart the db and the jetty openroberta server
### END INIT INFO

BASE=/data/openroberta
SCRIPTS=$BASE/conf/scripts
case "$1" in
    start)   bash $SCRIPTS/run.sh startAll ;;
    stop)    bash $SCRIPTS/run.sh stopAll  ;;
    restart) bash $SCRIPTS/run.sh startAll ;;
    *)       echo "Usage: $0 {start|stop|restart}"
             exit 12 ;;
esac
```
