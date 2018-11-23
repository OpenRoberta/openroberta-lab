#!/bin/bash

if [[ ! $(cat /proc/1/sched | head -n 1 | grep init) ]]
then
   echo 'running in a docker container :-)'
else
   echo 'not running in a docker container - exit 1 to avoid destruction and crash :-)'
   exit 1
fi
BRANCH=$1
if [ -z "$BRANCH" ]
then
    echo 'the branch to build is missing (first parameter) - exit 12'
    exit 12
fi
VERSION="$2"
if [ -z "$VERSION" ]
then
    echo 'the version parameter of form x.y.z is missing (second parameter) - exit 12'
    exit 12
fi
echo "building branch $BRANCH with version $VERSION [version is unused as long as no db-its are executed]"
cd /opt
git clone --depth=1 -b $BRANCH https://github.com/OpenRoberta/robertalab.git
cd /opt/robertalab/OpenRobertaParent
chmod +x RobotArdu/resources/linux/arduino-builder RobotArdu/resources/linux/tools-builder/ctags/5.8*/ctags
chmod +x RobotArdu/resources/linux-arm/arduino-builder RobotArdu/resources/linux-arm/tools-builder/ctags/5.8*/ctags

mvn clean install -DskipTests -DskipITs

# execute all tests, including the integration tests
mvn install -PrunIT
RC=$?
echo "maven return code is $RC"
 
case $RC in
  0) echo "returning SUCCESS"
     exit 0 ;;
  *) echo "returning ERROR"
     exit 16 ;;
esac