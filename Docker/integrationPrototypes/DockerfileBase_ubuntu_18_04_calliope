FROM ubuntu:18.04

ARG LAST_RUN_OF_BASE=2018-12-07

RUN apt-get update && apt-get -y upgrade && \
    apt-get install -y git maven && \
    apt-get install -y python-pip && \
    apt-get install -y libusb-0.1-4 && \
    apt-get install -y srecord libssl-dev && \
    pip install uflash && \
    apt-get update && apt-get -y upgrade && \
    apt-get install -y openjdk-8-jdk && \
    update-java-alternatives -s java-1.8.0-openjdk-amd64 && \
    apt-get install -y wget && \
    apt-get install -y apt-transport-https ca-certificates gnupg2 software-properties-common && \
    apt-get clean

RUN add-apt-repository ppa:team-gcc-arm-embedded/ppa && \
    apt-get update

RUN apt-get install -y gcc-arm-embedded && \
    apt-get clean

RUN git config --global core.fileMode false && \
    git config --global user.email "reinhard.budde@iais.fraunhofer.de" && \
    git config --global user.name "Reinhard Budde"

RUN apt-get install -y python-setuptools  cmake build-essential ninja-build python-dev libffi-dev libssl-dev

RUN pip install yotta

RUN git clone https://github.com/calliope-mini/calliope-demo.git

WORKDIR /calliope-demo/

COPY module.json .

RUN yt up &&  yt build