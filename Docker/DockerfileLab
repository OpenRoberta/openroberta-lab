FROM rbudde/openroberta_base:1

ARG LAST_RUN_OF_BASE=2018-11-16

EXPOSE 1999

RUN mkdir --parent /opt/openRoberta/lib /opt/openRoberta/OpenRobertaParent
WORKDIR /opt/openRoberta

COPY ["lib/","./lib/"]
COPY ["staticResources/","./staticResources/"]
COPY ["OpenRobertaParent/","./OpenRobertaParent/"]

ENTRYPOINT ["java", "-cp", "lib/*", "de.fhg.iais.roberta.main.ServerStarter", \
            "-d", "database.mode=server", \
            "-d", "server.staticresources.dir=staticResources", \
            "-d", "server.tutorial.dir=OpenRobertaParent/tutorial" \
           ]