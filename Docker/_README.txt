HOW TO GET AN OPENROBERTALAB INSTALLATION WITHOUT MUCH SETUP
... assuming you have installed docker and docker-compose ...
=============================================================

Variables used (set as needed!):

export VERSION='3.0.3'
export BRANCH=reinhardTest
export GITREPO=~rbudde/git/robertalab
export DISTR_DIR='/tmp/distr'
export DB_PARENTDIR='/home/rbudde/db'
export SERVER_PORT_ON_HOST=7000
export DBSERVER_PORT_ON_HOST=9001

tl;dr: to generate the docker image "rbudde/openroberta_lab:$VERSION" from the actual sources, run
       docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $BRANCH $VERSION

       Assuming that the environment variable DB_PARENTDIR holds the name of the directory, which
       contains the database directories (e.g. contains the directory db-$VERSION), then:
	   1. upgrade the db:                    docker run -v $DB_PARENTDIR:/opt/db rbudde/openroberta_upgrade:$BRANCH-$VERSION
	   2. start server and database server:  docker-compose -p ora -f dc-server-db-server.yml up &
	   3. stop both servers later:           docker-compose -p ora -f dc-server-db-server.yml stop

1. GENERATE THE "gen" IMAGE. THIS IS DOCUMENTATION. YOU MUST NOT DO THIS.

the docker image "gen", when run, generates an OpenRoberta distribution. The image contains installations of
- wget, curl
- git
- java
- maven
Furthermore during image creation a maven build is executed for branch develop to fill the /root/.m2 cache.
This makes later builds much faster.

cd $GITREPO/Docker
docker build -f DockerfileGen -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1

2. GENERATE THE "base" IMAGE. IT CONTAINS SOFTWARE FOR CROSSCOMPILATION. THIS IS DOCUMENTATION. YOU MUST NOT DO THIS.

cd $GITREPO/Docker
docker build -f DockerfileBase -t rbudde/openroberta_base:1 .
docker push rbudde/openroberta_base:1

3. NOW EVERYTHING IS READY TO CREATE A DISTRIBUTION:

Run the "gen" image. It will
- fetch the branch declared as first parameter
- generate the images for the version given as second parameter
- execute a maven build to generate the artifacts
- export the artifacts into a installation directory
- create several docker images:
  - "openroberta_lab" contains a server ready to co-operate with a db server
  - "openroberta_db" contains a production-ready db server
  - "openroberta_upgrade" contains an administration service working with an embedded database
    to upgrade the database
  - "openroberta_embedded" contains a server working with an embedded database
  - "openroberta_emptydbfortest" contains for test/debug a container with an empty database
  
When the "gen" image is run,
- the first -v arguments makes the "real" docker demon available in the "gen" container.
  Do not change this parameter
- a second -v is optional. If you want to get only a docker images, dismiss the parameter.
  If you want to access the installation directory (for testing, e.g.), then
  add -v $DISTR_DIR:/opt/robertalab/DockerInstallation to the docker run command. Set DISTR_DIR to an
  NOT EXISTING directory of the machine running the docker demon and you get the installation there

docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $BRANCH $VERSION 
	   
# the following commands are executed by the roberta maintainer; you should NOT do this
docker push rbudde/openroberta_lab:$BRANCH-$VERSION
docker push rbudde/openroberta_db:$BRANCH-$VERSION
docker push rbudde/openroberta_upgrade:$BRANCH-$VERSION
docker push rbudde/openroberta_embedded:$BRANCH-$VERSION
docker push rbudde/openroberta_emptydbfortest:$BRANCH-$VERSION

4. RUN THE SERVER

4.1 Upgrading the database

Assume that the exported environment variable DB_PARENTDIR contains a valid data base directory, e.g. db-$VERSION,
then run the upgrader first, if a new version is deployed (running it, if nothing has to be updated, is a noop):
  docker run -v $DB_PARENTDIR:/opt/db rbudde/openroberta_upgrade:$BRANCH-$VERSION

4.2 EMBEDDED SERVER
Start the server with an embedded database (no sqlclient access during operation, otherwise fine) 
- with docker:
  docker run -p 7100:1999 -v $DB_PARENTDIR:/opt/db rbudde/openroberta_embedded:$BRANCH-$VERSION &
- with docker-compose (using compose for a single container may appear a bit over-engineered):
  cd $GITREPO/Docker
  docker-compose -p ora -f dc-embedded.yml up -d &
Using docker-compose is preferred.
If the log message is printed, which tells you how many programs are in the data base, everything is fine and you can
access the server at http://dns-name-or-localhost:7100 (see docker command and the compose file)

4.2 SERVER AND DATABASE SERVER
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
Stop the two applications is done with
  docker-compose -p <project name> -f dc-server-db-server.yml stop
For the two services two different networks are created (inspect the output of "docker network ls"), IP ranges are separated (inspect
the output of "docker network inspect ora1_default" resp "docker network inspect ora2_default")

Note: when the container terminate, the message "... exited with code 130" is no error, but signals termination with CTRL-C

4. INTEGRATION TEST CONTAINER

Using the configuration file DockerfileIT you create an image, that contains
- all crosscompiler
- mvn and git
and has executed a
- git clone and
- mvn clean install
The entrypoint is defined as the bash script "runIT.sh".

  cd $GITREPO/Docker
  docker build -t rbudde/openroberta_it:1 -f DockerfileIT . --build-arg BRANCH=$BRANCH

The following commands are executed by the roberta maintainer; you should NOT do this
  docker push rbudde/openroberta_it:1
 
Starting this image
- clones the branch $BRANCH
- execute all tests, including the integration tests
- in case of success it returns 0, in case of errors/failures it returns 16

  docker run rbudde/openroberta_it:1 $BRANCH $VERSION

  
5. TEST AND DEBUG 

For test and debug, especially with cross compiler stuff, you want to run an image, that contains
- all crosscompiler
- mvn and git
and has executed a
- git clone and
- mvn clean install
The entrypoint is "/bin/sh".

This image is build by

  cd $GITREPO/Docker
  docker build -t rbudde/openroberta_debug:1 -f DockerfileDebug .

The following commands are executed by the roberta maintainer; you should NOT do this
  docker push rbudde/openroberta_debug:1
 
Start this image by

  docker run -p 7100:1999 -p 1099:1099 -it --entrypoint /bin/bash rbudde/openroberta_debug:1
  
It runs /bin/bash and you probably will
- pull from the repo                    git checkout reinhardTest; git pull
- build the server                      cd OpenRobertaParent;mvn clean install -DskipTests;cd ..
- create an empty database by executing ./ora.sh --createEmptydb
- for remote debugging                  nohup jstatd -J-Djava.security.policy=/opt/security/jstatd.all.policy &
- run the server using executing        ./ora.sh --start-from-git

  