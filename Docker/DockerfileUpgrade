FROM rbudde/openroberta_base:1

ARG LAST_RUN_OF_BASE=2018-12-19
 
RUN mkdir --parent /opt/openRoberta/lib /opt/openRoberta/OpenRobertaParent
WORKDIR /opt/openRoberta

VOLUME /opt/db
 
COPY ["lib/","./lib/"]
COPY ["OpenRobertaParent/","./OpenRobertaParent/"]

ENTRYPOINT ["java", "-cp", "lib/*", "de.fhg.iais.roberta.main.Administration", "upgrade", "/opt/db"]