#!/bin/bash

echo "run.sh [-q] [-D]                                                          quiet (first parameter, if used) and debug mode (next parameter, if used)"
echo "       help |                                                             this text"
echo "       docker-info | network | logs | test-info                           container state, network and some log from running containers; info about deployed servers"
echo "       gen <server> | start <server> | stop <server> | deploy <server> |  gen builds image, start first tries to stop, deploy is gen&start"
echo "       admin <server> <admin-cmd>                                         execute admin command on server, e.g. 'cleanup-temp-user-dirs'"
echo "       auto-deploy |                                                      check for git changes for servers found in variable AUTODEPLOY"
echo "       start-all | stop-all |                                             start/stop db server and server found in variable SERVERS"
echo "       gen-dbc | start-dbc | stop-dbc | backup [<db>]                     generate database server, start and stop server using variable DATABASES"
echo "       gen-net |                                                          generate the docker network $DOCKER_NETWORK_NAME"
echo "       prune                                                              rm as much unused data from docker as possible"
echo "       alive <dns> [<always-mail:default-is-true>]                        is server alive, e.g. lab.open-roberta.org? Send mail always or only when not alive"
