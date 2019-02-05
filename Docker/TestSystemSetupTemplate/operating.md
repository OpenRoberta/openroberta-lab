# Operating the test server (2019-02-01 15:00:00)

The test server is build for serving many different instances of the openroberta lab server. These instances are generated from commits of one or more git repositories.
Each instance of the openroberta lab server is running in a docker container of its own. Each instance of the openroberta lab server is connected to a database
dedicated to this openroberta lab server. All these databases are published by one database server running in a docker container of its own.

The main duties if you are using this test deployment template (for variables see below):

* for all test servers (with name <NAME>) you want to run, change in the directories $SERVER/<NAME> the file descl.sh as you need it.
* put the names of all test servers, which should be started, when the system boots, into file `$SERVER/servers.txt` (e.g. `test dev dev1`)
* in directory $BASE/db put all databases (the databases have the same name os the servers), which should be started when the system boots, into file
  `databases.txt` (e.g. `test dev dev1`)

Assuming that the network `ora-net` is created, the database container is running, then you (re-)deploy `test` and `dev1` and view the system state by executing

```bash
$CONF/run.sh deploy test # shorthand for gen and start

$CONF/run.sh gen dev1
$CONF/run.sh start dev1

$CONF/run.sh info
```

## Apache2 configuration

We need a running web server to distribute requests to the different openroberta lab server. This is done with Apache (nginx would be fine, too).
Configuration examples exist in the directory apache2:

* `localhost:1999` --- `test.open-roberta.org.443` and `test.open-roberta.org.80`
* `localhost:1998` --- `dev.open-roberta.org.443`  and `dev.open-roberta.org.80`
* `localhost:1997` --- `dev1.open-roberta.org.443` and `dev1.open-roberta.org.80`

and so on. B.t.w.: the ports are not fixed. But they must be consistent between web server and lab server configuration.

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

See `$SERVER/dev1` for example. From the declaration file the docker image and the running container for this server are generated:

* test: image `rbudde/openroberta_lab_test:1` and container with name `test`
* dev: image `rbudde/openroberta_lab_dev:1` and container with name `dev`
* dev1 up to dev9: image `rbudde/openroberta_lab_dev1:1` up to `rbudde/openroberta_lab_dev9:1` and container with name `dev1` up to `dev9`

Generating the image is roughly done by using the data from `decl.sh`:

* (optionally) pulling actual data into a Git repo
* checking out the branch/commit as declared
* running `mvn clean install -DskipTests`
* exporting the server into an export directory
* creating the docker image from that directory

Note: by using `flock` the generation of images is done sequentially. A new generation request is blocked as long as another generation is running.
Thus it is safe, that many test server share a git repository. This increases the performance of server generation a lot.

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

Maintenance and debugging commands are:

* `autoDeploy`: usually called from cron. Reads the server names from file `$SERVER/autodeploy.txt` and re-deploys each server, if the git
  repository connected to this server has got new commits. A typical line in crontab to run this is:
  
```bash
*/5 * * * * bash /data/openroberta/conf/scripts/run.sh -q autoDeploy >>/data/openroberta/logs/cronlog.txt
```

* `restart`: usually called from the configuration file `robertalab.sh` found in `/etc/init.d`. A typical script is:

```bash
...
```

* `info`: show images and running containers
* `logs`: show the last 10 lines of all logs of all running containers. B.t.w.: `docker logs -f <name>` is convenient to see how a server works.
* `prune`: remove all stale data from the docker installation. IMPORTANT if you are low with disc space.

Rarely used are commands:

* `genDbC`: generate the database container `rbudde/openroberta_db:2.4.0`. Used once when the test server is setup.
* `startDbC [<name1> ... <nameN>]`: (re-)start the database container servicing the databases given as parameters. The database names must match server names.
  This commmand is needed if a new server is added to the test server and a new database has been added to `$BASE/db`. If no database names are given,
  _all_ databases found in file `BASE/db/databases.txt` are taken into account
* `stopDbC`: stop the database container
