#!/bin/bash

echo "run.sh [-q] [-D]                                                               quiet (first parameter, if used) and debug mode (next parameter, if used)"
echo "       help |                                                                  this text"
echo "       info | network | logs |                                                 container state, network and few lines from all running containers"
echo "       gen <server> | start [<server>] | stop [<server>] | deploy [<server>] | start first tries to stop, deploy is gen&start"
echo "       autoDeploy |                                                            check for git changes for servers found in file server/autodeploy.txt"
echo "       startAll | stopAll |                                                    start/stop db server and server found in file server/servers.txt"
echo "       genDbC | startDbC | stopDbC |                                           generate database server, start and stop server using db/databases.txt"
echo "       genNet |                                                                generate the docker network ora-net"
echo "       prune                                                                   rm as much unused data from docker as possible"
