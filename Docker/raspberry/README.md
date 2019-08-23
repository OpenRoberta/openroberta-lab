# Dockerfile for raspberry

The docker file in this folder was tested on Raspberry pi 4, but it should also work on Raspberry pi 2 and 3.

> Since the fix needed to compile c4ev3 programs on raspberry hasn't been published yet, the Dockerfile
> contains 3 lines that references 3 folders/files that need to be in this folder before creating the image.
> The files are:
> - recent version of ora-cc-rsc
> - recent version of RobotEV3.jar
> - C4EV3.Toolchain-2019.08.0-rpi.tar.gz

## Install docker on raspberry

```bash
curl -sSL get.docker.com | sh
```

## Start container

The image of this dockerfile hasn't been published, so you need to run the build.

To start the build, cd into the folder that contains the Dockerfile and:

```bash
docker build -t ora .
```

Then:

```bash
mkdir admin
docker run -d -p 1999:1999 -v admin:/opt/OpenRoberta/admin ora
```

## Testing

To verify the image and to ensure that the cross-compilers don't break each other, the Dockerfile will
run the tester script at the end of the build.
The tester script tries to compile an empty program for each robot plugin, using
the HTTP interface (like the browser would do).
