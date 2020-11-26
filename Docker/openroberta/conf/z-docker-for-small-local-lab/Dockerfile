ARG FROM
FROM $FROM

VOLUME /opt/admin /opt/db
EXPOSE 1999

RUN mkdir --parent /opt/openroberta-lab/lib /tmp/openrobertaTmp
WORKDIR /opt/openroberta-lab

COPY ["./","./"]
RUN chmod ugo+rx ./*.sh

ENTRYPOINT ["./admin.sh","start-server"]
