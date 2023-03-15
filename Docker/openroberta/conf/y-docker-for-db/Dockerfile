FROM ubuntu:22.04

ENV LANG de_DE.UTF-8
ENV LANGUAGE de_DE:de
ENV LC_ALL de_DE.UTF-8

WORKDIR /opt/openRoberta
VOLUME /opt/db /opt/dbAdmin
EXPOSE 9001

COPY ["startDbServer.sh","./"]

RUN apt-get update && \
    apt-get install --no-install-recommends -y openjdk-8-jdk && \
    mkdir --parent /opt/openRoberta/lib && \
    chmod +x ./startDbServer.sh

COPY ./*.jar ./lib/

ENTRYPOINT ["/opt/openRoberta/startDbServer.sh"]
CMD []