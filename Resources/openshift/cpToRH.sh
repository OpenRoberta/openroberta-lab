#!/bin/sh

# it is expected that this script is executed with the 'openRoberta' git working directory as its base directory
SOURCE=.
TARGET=../redhatOpenRoberta

rm -rf $TARGET/diy
./ora.sh --export -i $TARGET/diy
cp -r $SOURCE/OpenRobertaServer/db $TARGET/diy/db
cp $SOURCE/Resources/openshift/action_hooks/* $TARGET/.openshift/action_hooks/