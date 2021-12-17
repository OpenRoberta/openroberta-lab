ARG FROM
FROM $FROM

VOLUME /opt/admin /tmp/openrobertaTmp
EXPOSE 1999

RUN mkdir --parent /opt/openroberta-lab/lib
WORKDIR /opt/openroberta-lab

COPY ["./","./"]
RUN chmod ugo+rx ./*.sh

ENTRYPOINT ["./start.sh"]
CMD []