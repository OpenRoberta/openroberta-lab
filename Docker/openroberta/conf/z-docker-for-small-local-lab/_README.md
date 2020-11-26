# instructions to create an image and run a container with a small local lab (for demo, ..., ...)

You should be familiar with the setup of the openroberta-lab (README.md in the Github repo) and the docker setup (_README.md in the Docker directory) 

This text describes
- how to run the image for the openroberta lab. This image contains all the cross compiler for all robots and runs on both linux and windows10.
- how to create a simple, complete stand-alone image for the openroberta lab. Probably you have NOT to do this. Get a pre-build image from dockerhub!

Note, that
- docker must be installed (either on windows10 or linux). Google for it, the setup is well-documented and straight-forward.
- Currently only support for `x64` machines. If you need a `arm32v7` version, for instance, please mail to reinhard.budde AT iais.fraunhofer.de

# run the image

when docker is installed and the image (with version 4.0.8) is pulled from dockerhub or built locally, the lab is started by the command
```bash
docker run -d -p 1999:1999 openroberta/standalone:4.0.8
```


# create the image

- git, mvn, java must be installed to create the image
- start from the base directory of your local clone of the openroberta-lab, called `BASE_DIR`
- you need a path of a non-existing directory for exporting the resources, called `EXPORT_DIR`
- from dockerhub get the latest version number of the repo `openroberta/base-x64:*`, called `BASE_VERSION`

```bash
BASE_VERSION=25                             # see remark above 
BASE_DIR='/D/git/openroberta-lab'           # my choice
EXPORT_DIR='/D/tmp/openroberta-lab-export'  # my choice
VERSION='4.0.8'                             # my choice, should match the version of the pom.xml

cd ${BASE_DIR}
mvn clean install -DskipTests
./ora.sh export ${EXPORT_DIR} gzip          # export the build

cd ${EXPORT_DIR}
export MSYS_NO_PATHCONV=1                   # if you using git bash on windows10 you need this
FROM="openroberta/base-x64:${BASE_VERSION}"
DOCKER_FILE="${BASE_DIR}/Docker/openroberta/conf/z-docker-for-small-local-lab/Dockerfile"
cp ${DOCKER_FILE} .
docker build --no-cache --tag openroberta/standalone:${VERSION} --build-arg FROM=${FROM} .
docker push openroberta/standalone:${VERSION}
cd ${BASE_DIR}
rm -Ir ${EXPORT_DIR}
```

