#!/bin/sh

# copy all relevant data into a git repository setup for openshift. The following assumptions are made:
# - this script resides in the Resource project of the robertalab git repository in the directory 'openshift'
# - it is expected, that this script is executed with this 'openshift' directory as its base directory
# - then two variables are set
#   - SOURCE is the path to the robertalab git repository, and the name is 'robertalab'
#   - TARGET is the path to the openshift git repository, that is used to launch the container
# - it is expected for safety reasons, that
#   - the last element of the path of SOURCE is 'robertalab'
#   - the last element of the path of TARGET is 'redhatOpenRoberta'

cd ../..                   # now at the root of the robertalab git repository
SOURCE=.
TARGET=../redhatOpenRoberta

if [ 1 -eq 1 ]             # you may disable the safety check
then
   if [ ! -d "$SOURCE/Resources" -o ! -d "$SOURCE/Resources/openshift" ]
   then
     echo 'Script terminates for safety reasons (SOURCE directory invalid)'
     exit 12
   fi
   if [ ! -d "$TARGET" ]
   then
     echo 'Script terminates for safety reasons (TARGET directory not found)'
     exit 12
   fi
fi

rm -rf $TARGET/diy
# db server actually does NOT work with openshift. Contact rbudde.
./ora.sh --databaseurl jdbc:hsqldb:file:db/openroberta-db --export -i $TARGET/diy
cp -r $SOURCE/OpenRobertaServer/db $TARGET/diy/db
cp $SOURCE/Resources/openshift/action_hooks/* $TARGET/.openshift/action_hooks/

if [ 1 -eq 1 ]             # you may enable/disable the automatic deployment
then
   echo 'deployment started'
   echo '=================='
   MESSAGE='deploy'
   cd $TARGET
   git add --all
   git commit -m "$MESSAGE"
   git push
fi
