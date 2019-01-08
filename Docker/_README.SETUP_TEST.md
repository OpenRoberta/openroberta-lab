# generate the lab server only and start OpenRoberta with docker and docker-compose

to get the complete picture, read the file _README.md !

```bash
export HOME="/home/TestOpenRoberta"
export VERSION='3.0.4'
export BRANCH='develop'
export GITREPO="$HOME/robertalab"
export DB_PARENTDIR="$HOME/export"
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