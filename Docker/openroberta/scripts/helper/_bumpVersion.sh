#!/bin/bash

# change the version of one or all (if multimodule project) poms and optionally commit that
# $1 the directory with the (parent) pom
# $2 version to set
# $3 message for commit (IF missing, NOTHING is committed)
PARENT="$1"
VERSION="$2"
MESSAGE="$3"

CWD=$(pwd)
cd "${PARENT}"
mvn versions:set -DnewVersion="${VERSION}"
mvn versions:commit
cd "${CWD}"

if [ -z "${MESSAGE}" ]
then
	echo "version set to ${VERSION}. Not committed!"
else
	git add --all
	git commit -m "${MESSAGE}"
fi