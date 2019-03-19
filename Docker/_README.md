# preparation (done from time to time)

## generate the "gen" image. This image can generate an OpenRoberta distribution.

When the docker image "gen" is run, it GENERATES an OpenRoberta distribution. It is NO OpenRoberta distribution by itself.
It gets version numbers independent from the OpenRoberta versions. During image creation a maven build is executed for
branch develop to fill the /root/.m2 cache. This makes later builds much faster.

```bash
cd to-the-docker-directory-of-the-git-repo
docker build -f meta/DockerfileGen_ubuntu_18_04 -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1
```

## generate the "base" IMAGE. It contains the crosscompiler.

The docker image "base" is used as basis for further images. It replaces crosscompiler for calliope and arduino by newer versions,
because the crosscompiler packages are erroneous [28.11.2018]. Java 8 is installed, too (for ev3).

```bash
cd to-the-docker-directory-of-the-git-repo
docker build -f meta/DockerfileBase_ubuntu_18_04 -t rbudde/openroberta_base:1 .
docker push rbudde/openroberta_base:1
```
## generate the base for INTEGRATION TEST and DEBUG.

Using the configuration file DockerfileIT_* you create an image, that contains all crosscompiler, mvn and git.
It has executed a git clone and mvn clean install. The entrypoint is defined as the bash script "runIT.sh".
If called, it will checkout a branch and runs both the tests and the integration tests. Only ubuntu-18-04 is valid now.
The debian stretch distributions contains invalid crosscompilers packages. This image is build by

```bash
cd to-the-docker-directory-of-the-git-repo
docker build -t rbudde/openroberta_it_ubuntu_18_04:1 -f testing/DockerfileIT_ubuntu_18_04 . --build-arg BRANCH=$BRANCH
docker push rbudde/openroberta_it_ubuntu_18_04:1
```

For debug you want to run an image, that contains mvn and git and all crosscompiler.
It should have executed a git clone and run a mvn clean install to get the (outdated!) sources, but have a populated mvn cache.
The entrypoint is "/bin/bash". This image is build by

```bash
cd to-the-docker-directory-of-the-git-repo
docker build -t rbudde/openroberta_debug_ubuntu_18_04:1 -f testing/DockerfileDebug_ubuntu_18_04 .
docker push rbudde/openroberta_debug_ubuntu_18_04:1
```

# run the integration tests and start a debug container

## integration tests

The integration test container clones the branch $BRANCH, execute all tests, including the integration tests and
in case of success it returns 0, in case of errors/failures it returns 16

```bash
export BRANCH='develop'
docker run rbudde/openroberta_it_ubuntu_18_04:1 $BRANCH 1.2.3 # 1.2.3 is the db version and unused for tests
```

## debug container

```bash
docker run -p 7100:1999 -it --entrypoint /bin/bash rbudde/openroberta_debug_ubuntu_18_04:1
```

It starts a /bin/bash and you probably will either run a server:

```bash
git checkout develop; git pull;
cd OpenRobertaParent;mvn clean install -DskipTests;cd ..
./ora.sh --createEmptydb
./ora.sh --start-from-git
```

or you want to run the integration tests (but many other tasks are possible :-)

```bash
git checkout develop; git pull; git co anotherBranchToDebug
cd OpenRobertaParent; mvn clean install -PrunIT
```

# create the images for server, database, upgrade and embedded and run them

this functionality is deprecated. It may be re-used later. See the directory `TestSystemSetupTemplate` for a much more flexible test setup.

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

