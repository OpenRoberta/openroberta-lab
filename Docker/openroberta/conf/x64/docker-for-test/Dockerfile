ARG BASE_VERSION
FROM openroberta/base-x64:$BASE_VERSION

WORKDIR /opt

RUN apt-get update -yq \
    && apt-get install curl gnupg -yq \
    && curl -sL https://deb.nodesource.com/setup_12.x | bash \
    && apt-get install nodejs -yq \
    && npm install html-entities

# shallow clone of the repo. Run mvn once to install most all the artifacts needed in /root/.m2 to speed up later builds
ARG BRANCH=develop

RUN git clone --depth=1 -b $BRANCH https://github.com/OpenRoberta/openroberta-lab.git
WORKDIR /opt/openroberta-lab
RUN ( mvn clean install -PrunIT || echo '!!!!!!!!!! runIT crashed. Should NOT happen !!!!!!!!!!' ) && \
    cd /opt && rm -rf openroberta-lab && \
    cd /tmp && rm -rf -- *

# prepare the entry point
WORKDIR /opt
COPY ["./runIT.sh","./"]
RUN chmod +x ./runIT.sh
ENTRYPOINT ["/opt/runIT.sh"]