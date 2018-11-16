FROM openjdk:8u151-jdk

ARG LAST_RUN_OF_BASE=2018-11-16

RUN mkdir --parent /opt/openRoberta/lib
WORKDIR /opt/openRoberta

VOLUME /opt/db
EXPOSE 9001

COPY ["startDbServer.sh","./"]
RUN chmod +x ./startDbServer.sh
COPY ["lib/","./lib/"]

ARG version
ENV VERSION=${version}
ENTRYPOINT ["/opt/openRoberta/startDbServer.sh"]