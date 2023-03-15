ARG BASEVERSION=35

FROM --platform=$BUILDPLATFORM maven:3.8-adoptopenjdk-8 AS build

RUN mkdir -p /tmp/build && cd /tmp/build
WORKDIR /tmp/build
COPY . .
RUN mvn clean install -DskipTests && ./ora.sh export /tmp/build/export gzip

FROM openroberta/base-x64:$BASEVERSION

VOLUME /opt/db /tmp/openrobertaTmp
EXPOSE 1999

RUN mkdir --parent /opt/openroberta-lab/lib
WORKDIR /opt/openroberta-lab

COPY --from=build /tmp/build/export .
COPY Docker/openroberta/conf/y-standalone-lab/start.sh .
RUN chmod ugo+rx ./*.sh

ENTRYPOINT ["./start.sh"]
CMD []
