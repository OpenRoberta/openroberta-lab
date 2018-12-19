FROM rbudde/openroberta_base:1

ARG LAST_RUN_OF_BASE=2018-11-28

# shallow clone of the repo. Run mvn once to install most all the artifacts needed in /root/.m2 to speed up later builds
WORKDIR /opt
ARG BRANCH=develop
RUN git clone --depth=1 -b $BRANCH https://github.com/OpenRoberta/robertalab.git
WORKDIR /opt/robertalab/OpenRobertaParent
RUN chmod +x RobotArdu/resources/linux/arduino-builder RobotArdu/resources/linux/tools-builder/ctags/5.8*/ctags && \
    chmod +x RobotArdu/resources/linux-arm/arduino-builder RobotArdu/resources/linux-arm/tools-builder/ctags/5.8*/ctags && \
    ( mvn clean install -PrunIT || echo '!!!!!!!!!! runIT crashed. Should NOT happen !!!!!!!!!!' ) && \
    cd /opt && \
    rm -rf robertalab 

# prepare the entry point
WORKDIR /opt
COPY ["./runIT.sh","./"]
RUN chmod +x ./runIT.sh
ENTRYPOINT ["/opt/runIT.sh"]