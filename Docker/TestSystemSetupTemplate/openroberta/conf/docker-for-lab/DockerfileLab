FROM rbudde/openroberta_base:1

ARG LAST_RUN_OF_BASE=2019-01-29

EXPOSE 1999

RUN mkdir --parent /opt/openRoberta/lib /opt/openRoberta/OpenRobertaParent
WORKDIR /opt/openRoberta

COPY ["start.sh","./"]
RUN chmod ugo+rx ./start.sh

COPY ["lib/","./lib/"]
COPY ["staticResources/","./staticResources/"]
COPY ["OpenRobertaParent/","./OpenRobertaParent/"]

ENTRYPOINT ["./start.sh"]
CMD []