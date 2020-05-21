FROM openjdk:8u151-jdk

RUN mkdir --parent /opt/openRoberta/lib
WORKDIR /opt/openRoberta

VOLUME /opt/db
VOLUME /opt/dbAdmin
EXPOSE 9001

COPY ["startDbServer.sh","./"]
RUN chmod +x ./startDbServer.sh
COPY ["*.jar","./lib/"]

ENTRYPOINT ["/opt/openRoberta/startDbServer.sh"]
CMD []