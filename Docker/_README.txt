HOW TO GET AN OPENROBERTALAB INSTALLATION WITHOUT MUCH SETUP
... assuming you have installed docker and docker-compose ...

Variables used (set as needed!):
export GITREPO=~rbudde/git/robertalab
export VERSION='2.5.3'
export DISTR_DIR='/tmp/distr'
export DB_PARENTDIR='/home/rbudde/db'
export SERVER_PORT_ON_HOST=7000
export DBSERVER_PORT_ON_HOST=9001

tl;dr: 1. to generate an actual docker image "rbudde/openrobertalab:$VERSION" from the sources, run
       docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $VERSION

       Assuming that the environment variable DB_PARENTDIR holds the name of the directory, which
       contains the database directories (e.g. contains the directory db-$VERSION), then ...
	   2. to start the embedded openrobertalab server run
	      docker-compose -f dc-embedded.yml up
	   3. to start the openrobertalab server and a separate database server run
	      docker-compose -f dc-server-db-server.yml up

1. generate the "gen" image. This is documentation, you must NOT do this

the docker image "gen", when run, generates an OpenRoberta distribution. The image contains installations of
- wget, curl
- git
- java
- maven
Furthermore during image creation a (then unused) maven build is executed to fill the /root/.m2 cache.
This makes later builds much faster.

cd $GITREPO/Docker
docker build -f DockerfileGen -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1

2. generate the "base" image. It contains software for crosscompilation. This is documentation, you must NOT do this

cd $GITREPO/Docker
docker build -f DockerfileBase -t rbudde/openroberta_base:1 .
docker push rbudde/openroberta_base:1

3. NOW EVERYTHING IS READY TO CREATE A DISTRIBUTION:

Run the "gen" image. It will
- retrieve the develop branch of the openroberta-lab from github
- execute a maven build to generate the openrobertalab artifacts
- export these artifacts into a installation directory
- create docker images:
  - "openrobertalab" contains a server ready to co-operate with a db server
  - "openrobertadb" contains a production-ready db server
  - "openrobertalaupgrade" contains an administration service working with an embedded database
    to upgrade the database
  - "openrobertalabembedded" contains a server working with an embedded database
  - "openrobertaemptydbfortest" contains for test/debug a container with an empty database
  
When the "gen" image is run,
- the first -v arguments makes the "real" docker demon available in the "gen" container.
  Do not change this parameter
- a second -v is optional. If you want to get only a docker images, dismiss the parameter.
  If you want to access the installation directory (for testing, e.g.), then
  add -v $DISTR_DIR:/opt/robertalab/DockerInstallation to the docker run command. Set DISTR_DIR to an
  NOT EXISTING directory of the machine running the docker demon and you get the installation there

docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $VERSION
	   
# the following commands are executed by the roberta maintainer; you should NOT do this
docker push rbudde/openrobertalab:$VERSION
docker push rbudde/openrobertadb:$VERSION
docker push rbudde/openrobertalabupgrade:$VERSION
docker push rbudde/openrobertalabembedded:$VERSION
docker push rbudde/openrobertaemptydbfortest:$VERSION

5. RUN THE SERVER

5.1 EMBEDDED SERVER
Assume that the exported environment variable DB_PARENTDIR contains a valid data base directory, e.g. db-$VERSION,
then run the upgrader first, if a new version is deployed (running it, if nothing has to be updated, is a noop):
  docker run -v $DB_PARENTDIR:/opt/db rbudde/openrobertaupgrade:$VERSION
and then start the server with an embedded database (no sqlclient access during operation, otherwise fine) 
- with docker:
  docker run -p 7100:1999 -v $DB_PARENTDIR:/opt/db rbudde/openrobertalabembedded:$VERSION &
- with docker-compose (using compose for a single container may appear a bit over-engineered):
  cd $GITREPO/Docker
  docker-compose -p ora -f dc-embedded.yml up &
Using docker-compose is preferred.
If the log message is printed, which tells you how many programs are in the data base, everything is fine and you can
access the server at http://dns-name-or-localhost:7100 (see docker command and the compose file)

5.2 SERVER AND DATABASE SERVER
Running two container, one db server container and one server container is the preferred way for production systems.
It allows the access to the database with a sql client (querying, but also backup and checkpoints):
  cd $GITREPO/Docker
  docker-compose -p ora -f dc-server-db-server.yml up -d
Remove the "-d" flag and append " &" to see detailed logging. Otherwise use docker logs to see logging output.
To stop the service, run
  cd $GITREPO/Docker
  docker-compose -p ora -f dc-server-db-server.yml stop
  
If you want to run two instances of the lab at the same time (do you really want to do this?), you start compose two times
and give each compose instance a different name. Of course you'll need two databases:
  cd $GITREPO/Docker
  SERVER_PORT_ON_HOST=7301 DBSERVER_PORT_ON_HOST=9301 DB_PARENTDIR=/tmp/ora1 docker-compose -p ora1 -f dc-server-db-server.yml up -d
  SERVER_PORT_ON_HOST=7302 DBSERVER_PORT_ON_HOST=9302 DB_PARENTDIR=/tmp/ora2 docker-compose -p ora2 -f dc-server-db-server.yml up -d
Stop the two applications is done with docker-compose -p <project name>.
For the two services two different networks are created (inspect the output of "docker network ls"), IP ranges are separated (inspect
the output of "docker network inspect ora1_default" resp "docker network inspect ora2_default")

5.3 RUNNING A TEST SETUP
  cd $GITREPO/Docker
  docker-compose -p test -f dc-testserver.yml up &
  ...
  docker-compose -p test -f dc-testserver.yml stop