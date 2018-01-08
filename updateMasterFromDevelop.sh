# ---------------------------------------------------------------------------------------------------------------------
# deployment script: deploy a version at master, setup develop for the next version.
# For suggestions and errors contact reinhard.budde@iais.fraunhofer.de
#
# ASSUMPTIONS:
# - maven project, may be hierarchical (parent + modules).
#   mvn MUST be on the PATH!
# - deploying a new master is done by a single person, i.e. there are NO conflicts when pushing master to the remote.
#   If this occurs, the conflicts have to be solved manually (as usually).
# - master and develop are clean. Develop is a descendant of master.
# ACTIONS:
# - develop is merged into master.
# - in master the version to be deployed is set in all poms. A tag is defined with the name of the deployment version.
# - in develop the next SNAPSHOT version should is set in all poms. No merge commit should occur (:-).
#   We keep a straight line of commits.
# From the assumptions as much as possible is checked to avoid any hassle.
# Everything is done locally, after the deploy script is run, pushing develop and master to the remote should be done QUICK :-)
# Running the script with the single parameter -push will do that
#
# For deployments, two parameters are mandatory (both WITHOUT SNAPSHOT)
# 1. the version number to be deployed on master
# 2. the next version to be set in develop (suffixed with "-SNAPSHOT") 
# ---------------------------------------------------------------------------------------------------------------------

# helper functions

# check whether the branch is clean. If not: exit 12
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
# to acces the parent pom in the current directory, set the variable PARENT to ''
function runMaven {
	VERSION=$1
	cd $PARENT
	mvn versions:set -DnewVersion=$VERSION
	mvn versions:commit
	cd $CWD
}

# remember working directory and directory with (parent) pom 
CWD=$(pwd)
if [ "$3" == '-p' ]
then
	PARENT="$4"
else
	PARENT="$CWD"
fi
echo parent $PARENT
if [ -d "$PARENT" -a -f "$PARENT/pom.xml" ]
then
	:
else
	echo "\"$PARENT\" is no valid directory for maven builds - exit 12"
	exit 12
fi

# start message
echo
echo 'parameter: <thisVersion> <nextVersion> [-p <directory with the (parent) pom>]'
echo '           deploy a version in master and set the next version in develop'
echo '           both versions WITHOUT "-SNAPSHOT"'
echo 'parameter: -push'
echo '           push develop and master to remote (small convenience script)'
echo '==================================================================================================='
echo
echo "working directory is $CWD"

# CONSISTENCY CHECKS (6)
# 1. mvn is on the path
MVN=$(mvn -version)
if [ -z "$MVN" ]
then
	echo "mvn is not on the path. Build is impossible - exit 12"
	exit 12
else
	MVN=$(echo "$MVN"|head -1);
	echo using "$MVN"
fi

# 2. git project
if [ -d .git ]
then
	:
else
	echo "this script only runs in a git directory - exit 12"
	exit 12
fi

# 3. we are in branch develop
BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$BRANCH" != 'develop' ];
then
	echo "this script only runs in the develop branch, but we are in $BRANCH - exit 12"
	exit 12
fi

# 4. develop and master are clean
checkClean
git checkout master
checkClean
git checkout develop

# 5. develop can be rebased on master (should be a noop!)
git rebase master
if [ $? -ne 0 ]
then
    git rebase --abort
	echo 'develop IS NO SAFE DESCENDANT OF master. In develop run "git rebase master" and solve the problem - exit 12'
	exit 12
fi

# convenience part to be used AFTER this script with version commits has been run and  consistency checks 1 to 5 succeeded.
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
  *)          echo "deploying this version $thisVersion" ;;
esac
nextVersion=$2
case "$nextVersion" in
  '')         echo 'next version parameter is missing - exit 12'
              exit 12 ;;
  *-SNAPSHOT) echo 'snapshot version is not legal in deployments - exit 12'
              exit 12 ;;
  *)          echo "preparing next version $nextVersion" ;;
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
git tag "V-$thisVersion" -m "Version $thisVersion"

git checkout develop
nextVersion=${nextVersion}-SNAPSHOT
runMaven "$nextVersion"
git add --all;git commit -m "next version is planned to be $nextVersion"

echo 'everything looks fine. You should are in branch develop and should push both develop and master now'
echo 'to do this you may call this script with the single parameter -push'