# deprecated functionality: create the server, database, upgrade and embedded images in a docker container and run them

this functionality is deprecated. It may be re-used later, but is not guaranteed to work as it is.
See the directory `TestSystemSetupTemplate` for a much more flexible test setup.

## generate the image for DEBUG

For debug you want to run an image, built upon the "base" image, that contains all crosscompiler, mvn and git.
It has executed a git clone of the main git repository `openroberta-lab` and has executed a `mvn clean install`. This is done to fill the
(mvn) cache and speeds up later builds considerably. The entrypoint is "/bin/bash". This image is build by

```bash
BASE_DIR=/data/openroberta-lab
BASE_VERSION=4
cd $BASE_DIR/conf/docker-for-test
docker build --build-arg BASE_VERSION=$BASE_VERSION \
       -t rbudde/openroberta_debug_ubuntu_18_04:$BASE_VERSION  -f DockerfileDebug_ubuntu_18_04 .
docker push rbudde/openroberta_debug_ubuntu_18_04:$BASE_VERSION
```

## run the DEBUG container

```bash
docker run -p 7100:1999 -it --entrypoint /bin/bash rbudde/openroberta_debug_ubuntu_18_04:$BASE_VERSION
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

## generate the "gen" image. This image can generate an OpenRoberta distribution

When the docker image "gen" is run, it GENERATES an OpenRoberta distribution. It is NO OpenRoberta distribution by itself.
It gets version numbers independent from the OpenRoberta versions. During image creation a further unused maven build is executed for
branch develop to fill the maven cache. This makes later builds much faster.

As the bash script `genLab.sh` is missing, this build will not succeed at the moment.

```bash
BASE_DIR=/data/openroberta-lab
BASE_VERSION=4
cd $BASE_DIR/conf/docker-for-meta
docker build -f DockerfileGen_ubuntu_18_04 -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1
```

## define the variables used (set as needed!)

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
