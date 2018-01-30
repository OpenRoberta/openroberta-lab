# ---------------------------------------------------------------------------------------------------------------------
# deployment script: deploy a version at master, setup develop for the next version.
# For suggestions and errors contact reinhard.budde at iais.fraunhofer.de
#
# For deployments, two parameters are mandatory (both WITHOUT SNAPSHOT)
# 1. the version number to be deployed on master
# 2. the next version to be set in develop (suffixed with "-SNAPSHOT") 
# E.g. ./updateMasterFromDevelop.sh 2.4.1 2.5.0
#
# ASSUMPTIONS:
# - maven project, may be hierarchical (parent + modules).
#   mvn is used and on the PATH!
# - the changes in master and develop can be done in a local repo first (+ checked for sanity) and then both are pushed
#   to the remote. During this time interval nobody should push to develop.
# - master and develop are clean. Develop is descendant of master.
#
# ACTIONS:
# - Develop is merged into master.
# - In master the version to be deployed is set in all poms. A tag is defined with the name of the deployment version.
# - In develop the next SNAPSHOT version is set in all poms.
# A goal of this script is to avoid merge commits. We want to keep a straight line of commits in master.
#
# Everything is done locally, after the deploy script is run, check for sanity and then push develop and master to
# the remote IMMEDIATELY :-). Running the script with the single parameter -push will do that push of develop and master
#
# ---------------------------------------------------------------------------------------------------------------------

# helper functions

# check whether the branch is clean.
function checkClean {
	if [ -z "$(git status --porcelain)" ];
	then
		:
	else
		BRANCH=$(git rev-parse --abbrev-ref HEAD)
		echo "please commit your changes in branch $BRANCH first. Exit 12"
		exit 12
	fi
}

# run maven to set a new version (given in $1).
# If a directory (with a pom) is defined, cd to that and at the end cd back.
function runMaven {
	VERSION=$1
	cd $PARENT
	mvn versions:set -DnewVersion=$VERSION
	mvn versions:commit
	cd $CWD
}

# ask a question. If user answers "y", everything is fine. Otherwise exit 1!
function question {
	echo -n "$1 (\"y\" if everything is ok) "
	read ANSWER
	case "$ANSWER" in
	  y) : ;;
	  *) exit 1 ;;
	esac
}

# =========================================================================================================================================
# this is the OpenRoberta specific part. It has been added to give hints what may have been forgotten when a new version has to be deployed
# ask a question. If user answers "y", everything is fine. Otherwise exit 1!
function question {
	echo -n "$1 (y if ok) "
	read ANSWER
	case "$ANSWER" in
	  y) : ;;
	  *) exit 1 ;;
	esac
}

tagPrefix=ORA-
question 'did you update the element <openRobertaServer.history> in the parent pom?'
question 'is a database upgrade necessary? Did you change the class Upgrader.java and the SQL script "create-tables.sql" if needed?'
question 'is an update of versions for the EV3 robots in RobotEV3/pom.xml (e.g. <ev3runtime.v0.version>) needed?' 
# =========================================================================================================================================

# remember working directory and directory with (parent) pom.
CWD=$(pwd)
if [ "$3" == '-p' ]
then
	PARENT="$4"
else
	PARENT="$CWD"
fi
echo "parent directory is $PARENT"
if [ -d "$PARENT" -a -f "$PARENT/pom.xml" ]
then
	:
else
	echo "\"$PARENT\" is no valid directory for maven builds - exit 12"
	exit 12
fi

# start message.
echo
echo './updateMasterFromDevelop.sh <thisVersion> <nextVersion> [-p <directory with the (parent) pom>]'
echo '           merge develop into master, giving <thisVersion> and prepare develop for <nextVersion>'
echo
echo "working directory is $CWD"

# CONSISTENCY CHECKS (6):
# 1. mvn is on the path.
MVN=$(mvn -version)
if [ -z "$MVN" ]
then
	echo "mvn is not on the path. Build is impossible - exit 12"
	exit 12
else
	MVN=$(echo "$MVN"|head -1);
	echo "using $MVN"
fi

# 2. is a git project.
if [ -d .git ]
then
	:
else
	echo "this script only runs in a git directory - exit 12"
	exit 12
fi

# 3. we are in branch develop.
BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$BRANCH" != 'develop' ];
then
	echo "this script only runs in the develop branch, but we are in $BRANCH - exit 12"
	exit 12
fi

# 4. develop and master are clean.
checkClean
git checkout master
checkClean
git checkout develop

# 5. develop is an descendant of master.
git merge-base --is-ancestor master develop
if [ $? -ne 0 ]
then
	echo 'develop IS NO DESCENDANT OF master. Solve this problem - exit 12'
	exit 12
fi

# convenience part to be used AFTER this script with version commits has been run and consistency checks 1 to 5 succeeded.
# Pushes master and develop to remote
if [ "$1" = '-push' ]
then
	echo 'Starting to push commits of develop and master to remote. Then develop is checked out again'
	git push
	git checkout master
	git push
	git checkout develop
	exit 0
fi

# 6. the version parameter are set and valid
thisVersion=$1
case "$thisVersion" in
  '')         echo 'this version parameter is missing - exit 12'
              exit 12 ;;
  *-SNAPSHOT) echo 'snapshot version is not legal in deployments - exit 12'
              exit 12 ;;
  *)          echo "master will become version $thisVersion" ;;
esac
nextVersion=$2
case "$nextVersion" in
  '')         echo 'next version parameter is missing - exit 12'
              exit 12 ;;
  *-SNAPSHOT) echo 'please remove the -SNAPSHOT - exit 12'
              exit 12 ;;
  *)          nextVersion="${nextVersion}-SNAPSHOT"
              echo "develop will be initialized to version ${nextVersion}" ;;
esac

# the workflow: this version -> develop; merge develop into master; next version snapshot to develop
runMaven "$thisVersion"
git add --all;git commit -m "deployment of version $thisVersion"
git checkout master
git merge develop
if [ $? -ne 0 ]
then
	echo 'when merging develop into master a merge conflicht occurred. Solve it first - exit 12'
	git merge --abort
	exit 12
fi
git tag "$tagPrefix$thisVersion" -m "Version $thisVersion"

git checkout develop

runMaven "$nextVersion"
git add --all;git commit -m "next version is planned to be $nextVersion"

echo 'everything looks fine. You are in branch develop and should push both develop and master'
echo 'to do this you may run ./updateMasterFromDevelop.sh -push'