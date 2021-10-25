ARG BASEVERSION=25

FROM maven:3.8-adoptopenjdk-8 AS build

RUN mkdir -p /tmp/build && cd /tmp/build
WORKDIR /tmp/build
COPY . .
RUN mvn clean install -DskipTests && ./ora.sh export /tmp/build/export gzip

FROM openroberta/base:$BASEVERSION

VOLUME /opt/admin
EXPOSE 1999

RUN mkdir --parent /opt/openroberta-lab/lib /tmp/openrobertaTmp
WORKDIR /opt/openroberta-lab

COPY --from=build /tmp/build/export .
COPY Docker/openroberta/conf/y-docker-for-lab-ci/*.sh .
RUN chmod ugo+rx ./*.sh

ENTRYPOINT ["./start.sh"]
CMD []
