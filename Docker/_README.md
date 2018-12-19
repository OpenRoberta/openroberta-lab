# how to get an OpenRoberta installation with docker and docker-compose

## define the variables used (set as needed!):

this is the (temporary) setting for the test server docker container

```bash
export HOME="/home/TestOpenRoberta"
export VERSION='3.0.4'
export BRANCH='develop'
export GITREPO="$HOME/robertalab"
export DB_PARENTDIR="$HOME/export"
export SERVER_PORT_ON_HOST=1999
export DBSERVER_PORT_ON_HOST=9001
```

this is the setting for docker tests of rbudde on ilya.iais.fraunhofer.de

```bash
export HOME="/home/rbudde"
export VERSION='3.0.r'
export BRANCH='rbTest'
export GITREPO="$HOME/git/robertalab"
export DISTR_DIR='/tmp/distr'
export DB_PARENTDIR="$HOME/db"
export SERVER_PORT_ON_HOST=7000
export DBSERVER_PORT_ON_HOST=9001
```

## tl;dr: to generate the docker image "rbudde/openroberta_lab:$VERSION" from "develop" run

```bash
docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $BRANCH $VERSION
```

Assuming that the environment variable DB_PARENTDIR holds the name of the directory, which contains the database
directories (e.g. contains the directory db-$VERSION), then OpenRoberta is started by

```bash
cd $GITREPO/Docker;                                                             # where the compose files are :-)
docker run -v $DB_PARENTDIR:/opt/db rbudde/openroberta_upgrade:$BRANCH-$VERSION # upgrade the db
docker-compose -p ora -f dc-server-db-server.yml up &                           # start server and database server
docker-compose -p ora -f dc-server-db-server.yml stop                           # stop both servers later  
```

# generate the "gen" image. THIS IS DOCUMENTATION. YOU MUST NOT DO THIS.

When the docker image "gen" is run, GENERATES an OpenRoberta distribution. It is NO OpenRoberta distribution by itself.
It gets version numbers independent from the OpenRoberta versions. During image creation a maven build is executed for
branch develop to fill the /root/.m2 cache. This makes later builds much faster.

```bash
cd $GITREPO/Docker
docker build -f meta/DockerfileGen_ubuntu_18_04 -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1
```

# generate the "base" IMAGE. It contains the crosscompiler. THIS IS DOCUMENTATION. YOU MUST NOT DO THIS.

The docker image "base" is used as basis for further images. It replaces crosscompiler for calliope and arduino by newer versions,
because the crosscompiler packages are erroneous [28.11.2018]. Java 8 is installed, too (for ev3).

```bashcd $GITREPO/Docker
docker build -f meta/DockerfileBase_ubuntu_18_04 -t rbudde/openroberta_base:1 .
docker push rbudde/openroberta_base:1
```

# Do this :-) 

Run the "gen" image. It will

* fetch the branch declared as first parameter
* generate the images for the version given as second parameter
* execute a maven build to generate the artifacts
* export the artifacts into a installation directory
* create several docker images, all based on the "base" images and this being based on ubuntu_18_04 (no third parameter when called or parameter is not 'false'):
  * "openroberta_lab" contains a server ready to co-operate with a db server
  * "openroberta_db" contains a production-ready db server
  * "openroberta_upgrade" contains an administration service working with an embedded database
    to upgrade the database
  * "openroberta_embedded" contains a server working with an embedded database
* OR create "openroberta_lab", which contains a server ready to co-operate with a db server (third parameter when called is 'false')

When the "gen" image is run,

* the first -v arguments makes the "real" docker demon available in the "gen" container.
  Do not change this parameter
- a second -v is optional. If you want to get only the docker images, dismiss the parameter.
  If you want to access the installation directory (for testing, e.g.), then
  add -v <DISTR_DIR>:/opt/robertalab/DockerInstallation to the docker run command. Set <DISTR_DIR> to an
  NOT EXISTING directory of the machine running the docker demon and you get the installation there
  for inspection

```bash
docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $BRANCH $VERSION
```
	   
# commands for the roberta maintainer. THIS IS DOCUMENTATION. YOU MUST NOT DO THIS.

```bash
docker push rbudde/openroberta_lab:$BRANCH-$VERSION
docker push rbudde/openroberta_db:$BRANCH-$VERSION
docker push rbudde/openroberta_upgrade:$BRANCH-$VERSION
docker push rbudde/openroberta_embedded:$BRANCH-$VERSION
```

# RUN THE SERVER

## Upgrading the database

Assume that the exported environment variable DB_PARENTDIR contains a valid data base directory, e.g. db-$VERSION,
then run the upgrader first, if a new version is deployed (running it, if nothing has to be updated, is a noop):

```bash
docker run -v $DB_PARENTDIR:/opt/db rbudde/openroberta_upgrade:$BRANCH-$VERSION
```

## embedded server

Start the server with an embedded database (no sqlclient access during operation, otherwise fine) either with
docker or with docker-compose (using compose for a single container may appear a bit over-engineered, but is preferred).

```bash
docker run -p 7100:1999 -v $DB_PARENTDIR:/opt/db rbudde/openroberta_embedded:$BRANCH-$VERSION &

cd $GITREPO/Docker
docker-compose -p ora -f dc-embedded.yml up -d &
```

If the log message is printed, which tells you how many programs are in the data base, everything is fine and you can
access the server at http://dns-name-or-localhost:7100 (see docker command and the compose file)

## server and database server

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

# INTEGRATION TEST CONTAINER

Using the configuration file DockerfileIT_* you create an image, that contains all crosscompiler, mvn and git.
It has executed a git clone and mvn clean install. The entrypoint is defined as the bash script "runIT.sh".
It will checkout a branch and runs both the tests and the integration tests. Only ubuntu-18-04 is valid now.
The debian stretch distributions contains invalid crosscompilers packages.

```bash
cd $GITREPO/Docker
docker build -t rbudde/openroberta_it_ubuntu_18_04:1 -f tesing/DockerfileIT_ubuntu_18_04 . --build-arg BRANCH=$BRANCH
```

The following command is executed by the roberta maintainer; you should NOT do this

```bash
docker push rbudde/openroberta_it_ubuntu_18_04:1
```
 
If you start this branch, the container clones the branch $BRANCH, execute all tests, including the integration tests and
in case of success it returns 0, in case of errors/failures it returns 16

```bash
docker run rbudde/openroberta_it_ubuntu_18_04:1 $BRANCH $VERSION
```

## TEST AND DEBUG 

For test and debug, especially cross compiler stuff, you want to run an image, that contains mvn and git and all crosscompiler.
It should have executed a git clone and run a mvn clean install to get the (outdated!) sources, but have a populated mvn cache.
The entrypoint is "/bin/bash" (this doesn't matter). This image is build by

```bash
cd $GITREPO/Docker
docker build -t rbudde/openroberta_debug_ubuntu_18_04:1 -f testing/DockerfileDebug_ubuntu_18_04 .
```

The following commands are executed by the roberta maintainer; you should NOT do this

```bash
docker push rbudde/openroberta_debug_ubuntu_18_04:1
```

Start this image by

```bash
docker run -p 7100:1999 -it --entrypoint /bin/bash rbudde/openroberta_debug_ubuntu_18_04:1
```

It runs /bin/bash and you probably will either run a server:

```bash
git checkout develop; git pull;
cd OpenRobertaParent;mvn clean install -DskipTests;cd ..
./ora.sh --createEmptydb
./ora.sh --start-from-git
```

or you want to run the integration tests (but many other tasks are possible :-)

```bash
git checkout develop; git pull; git co anotherBranchToDebug
cd OpenRobertaParent; mvn -PrunIT clean install
```
 