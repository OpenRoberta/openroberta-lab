FROM ubuntu:18.04

ARG LAST_RUN_OF_BASE=2018-12-19

RUN apt-get update && apt-get -y upgrade && \
    apt-get install -y openjdk-8-jdk && \
    update-java-alternatives -s java-1.8.0-openjdk-amd64 && \
    apt-get install -y apt-utils && \
    apt-get install -y apt-transport-https ca-certificates gnupg2 software-properties-common && \
    apt-get install -y git maven && \
    git config --global core.fileMode false && \
    git config --global user.email "reinhard.budde@iais.fraunhofer.de" && \
    git config --global user.name "Reinhard Budde"

RUN apt-get install -y curl && \
    curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | apt-key add - && \
    add-apt-repository \
            "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
            $(lsb_release -cs) \
            stable" && \
    apt-get update && \
    apt-get install -y docker-ce && \
    apt-get clean

# shallow clone of develop. Branch defaults to "develop". Run mvn once to install most all the artifacts needed in /root/.m2
WORKDIR /opt
RUN echo "cloning branch develop and executing mvn to fill the .m2 cache" && \
    git clone --depth=1 -b develop https://github.com/OpenRoberta/openroberta-lab.git && \
    cd /opt/openroberta-lab && \
    mvn clean install
 
# prepare the entry point
ARG LAST_RUN_OF_COPY_GENLAB=2019-01-08
WORKDIR /opt
COPY ["./genLab.sh","./"]
RUN chmod +x ./genLab.sh
WORKDIR /opt/openroberta-lab
ENTRYPOINT ["/opt/genLab.sh"]