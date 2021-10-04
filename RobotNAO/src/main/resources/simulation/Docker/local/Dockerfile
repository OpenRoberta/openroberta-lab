ARG WEBOTS_VERSION=R2021b-ubuntu20.04

FROM cyberbotics/webots:$WEBOTS_VERSION

RUN apt-get update
RUN apt-get install -y firejail python3-pip python-is-python3 firejail python3-pip subversion psmisc

RUN mkdir -p /home/cyberbotics/simulation && \
    cd /home/cyberbotics/ && svn export https://github.com/cyberbotics/webots/trunk/resources/web/server server && \
    cd /home/cyberbotics/server/ && \
    touch /home/cyberbotics/server/log/output.log

RUN pip3 install tornado pynvml psutil requests distro

ENV CONFIG=local

COPY . /home/cyberbotics/simulation
CMD xvfb-run /home/cyberbotics/simulation/Docker/local/start_server.sh
