#!/bin/bash

# all these -d parameter are independant from the server instance. Server-related -d parameter are added by '$*' from the script '_start.sh'
java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
     -d server.staticresources.dir=./staticResources \
     -d database.mode=server \
     -d database.uri=ora-db-server \
     -d robot.crosscompiler.resourcebase=/opt/ora-cc-rsc \
     -d server.admin.dir=/opt/admin \
	   $*