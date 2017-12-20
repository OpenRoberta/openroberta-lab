HOW TO GET AN OPENROBERTALAB INSTALLATION WITHOUT MUCH SETUP
... assuming you have installed docker and docker-compose ...

tl;dr: 1. to generate an actual docker image "rbudde/openrobertalab:2.4.0" from the sources, run
       docker run -v /var/run/docker.sock:/var/run/docker.sock rbudde/openroberta_gen:1

       Assuming that the environment variable DB_PARENTDIR holds the name of the directory, which
       contains the datbase directories (e.g. the directory db-2.4.0), then ...
	   2. to start the embedded openrobertalab server run
	      docker-compose -f dc-embedded.yml up
	   3. to start the openrobertalab server and a separate database server run
	      docker-compose -f dc-server-db-server.yml up

1. generate the "meta" image. This is documentation, you must NOT do this

the directory meta and its dockerfile are used to create the base image "meta", that contains
- wget, curl
- git
- java
- maven
During image creation a (then unused) maven build is executed to fill the /root/.m2 cache.
This makes later builds much faster. The image generated is pushed to the docker hub.

DOCKERDIR=~rbudde/docker
GITREPO=~rbudde/GitRepositories/robertalab

mkdir -p $DOCKERDIR/meta $DOCKERDIR/gen $DOCKERDIR/db
cd $GITREPO/Docker
cp DockerfileMeta $DOCKERDIR/meta/Dockerfile
cp DockerfileGen  $DOCKERDIR/gen/Dockerfile
cp ora_gen.sh     $DOCKERDIR/gen
cp -r $GITREPO/OpenRobertaParent/OpenRobertaServer/db-2.4.0 $DOCKERDIR/db

cd $DOCKERDIR/meta
docker build -t rbudde/openroberta_meta:1 .
docker push rbudde/openroberta_meta:1

2. generate the "gen" image. This is documentation, you must NOT do this

based on the "meta" image, the directory gen and its dockerfile are used to create the image "gen".
The generated image can be RUN. See the next section about what happens during a run.

cd $DOCKERDIR/gen
docker build -t rbudde/openroberta_gen:1 .
docker push rbudde/openroberta_gen:1

3. RUN THE "GEN" IMAGE TO CREATE AN ACTUAL VERSION OF THE OPENROBERTA-LAB

If the "gen" image runs, it
- retrieves the develop branch of the openroberta-lab from github
- executes a maven build to generate the openrobertalab artifacts
- exports these artifacts into a installation directory
- creates a docker image "openrobertalab" from the installation directory

The next sections describes how to use the image "openrobertalab".
When the "gen" image is run,
- the first -v arguments makes the "real" docker demon available in the "gen" container.
  Do not change this parameter
- the second -v is optional. If you want to get only a docker image, dismiss the 2 parts.
  If you want to access the installation directory (for testing, e.g.), then
  set DISTR_DIR to an EMPTY EXISTING directory of your machine (running the docker demon)
  and you get the installation:

DISTR_DIR=/tmp/distr
docker run -v /var/run/docker.sock:/var/run/docker.sock \
           -v $DISTR_DIR:/opt/robertalab-develop/DockerInstallation \
	   rbudde/openroberta_gen:1
docker push rbudde/openrobertalab:2.4.0 # done by the roberta maintainer; you should NOT do this

4. RUN THE EMBEDDED SERVER

Assume that $DOCKERDIR/db contains a valid data base directory, e.g. db-2.4.0, then you can run the image 
- with docker:
  docker run -p 7000:1999 -v $DOCKERDIR/db:/opt/db rbudde/openroberta_gen:2.4.0
- with docker-compose:
  cd $GITREPO/Docker # here the docker-compose.yml is located
  docker-compose up
Using docker-compose is preferred.
If the log message, that tells you how many programs are in the data base, is printed, you can access
the server pointing a browser to http://dns-name-or-localhost:7000

