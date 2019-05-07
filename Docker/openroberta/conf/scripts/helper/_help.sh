#!/bin/bash

echo "run.sh [-q] [-D]                                                               quiet (first parameter, if used) and debug mode (next parameter, if used)"
echo "       help |                                                                  this text"
echo "       docker-info | network | logs | test-info                                container state, network and some log from running containers; info about deployed servers"
echo "       gen <server> | start [<server>] | stop [<server>] | deploy [<server>] | start first tries to stop, deploy is gen&start"
echo "       autoDeploy |                                                            check for git changes for servers found in variable AUTODEPLOY"
echo "       startAll | stopAll |                                                    start/stop db server and server found in variable SERVERS"
echo "       genDbC | startDbC | stopDbC | backupDb [<db>]                           generate database server, start and stop server using variable DATABASES"
echo "       genNet |                                                                generate the docker network $DOCKER_NETWORK_NAME"
echo "       prune                                                                   rm as much unused data from docker as possible"
