ARG CCBIN_VERSION
FROM openroberta/ccbin-x64:${CCBIN_VERSION}

WORKDIR /opt/ora-cc-rsc/
# the ora--cc-rsc git repo has a .dockerignore to exclude unwanted resources
COPY ./ ./
ENV robot_crosscompiler_resourcebase=/opt/ora-cc-rsc/
