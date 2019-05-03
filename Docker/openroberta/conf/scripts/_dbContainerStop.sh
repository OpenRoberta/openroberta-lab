#!/bin/bash

CONTAINER=ora-${INAME}-db-server
echo "stopping the database container '${CONTAINER}', if running"
docker stop "${CONTAINER}" >>/dev/null
