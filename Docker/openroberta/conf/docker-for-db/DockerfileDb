FROM openjdk:8u151-jdk

ARG LAST_RUN_OF_BASE=2019-01-29

RUN mkdir --parent /opt/openRoberta/lib
WORKDIR /opt/openRoberta

VOLUME /opt/db
EXPOSE 9001

COPY ["startDbServer.sh","./"]
RUN chmod +x ./startDbServer.sh
COPY ["*.jar","./lib/"]

ENTRYPOINT ["/opt/openRoberta/startDbServer.sh"]