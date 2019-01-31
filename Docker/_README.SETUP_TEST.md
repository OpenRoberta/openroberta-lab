# generate the lab server only and start OpenRoberta with docker and docker-compose

to get the complete picture, read the file _README.md !

```bash
export HOME="/home/TestOpenRoberta"
export VERSION='3.0.4'
export BRANCH='develop'
export GITREPO="$HOME/robertalab"
export DB_PARENTDIR="$HOME/db"
export SERVER_PORT_ON_HOST=1999
export DBSERVER_PORT_ON_HOST=9001
export BUILD_ALL=false

echo 'generating all containers based on branch develop'
docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1 $BRANCH $VERSION $BUILD_ALL
docker images

echo 'stopping the old container and starting the new container'
cd $GITREPO/Docker
docker-compose -p ora -f dc-server-db-server.yml down
docker-compose -p ora -f dc-server-db-server.yml up -d
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

