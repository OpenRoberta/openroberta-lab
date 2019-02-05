#!/bin/bash

java -cp lib/\* de.fhg.iais.roberta.main.ServerStarter \
     -d server.staticresources.dir=./staticResources -d database.mode=server -d database.uri=ora-db-server \
	 $*