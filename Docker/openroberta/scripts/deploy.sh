echo '
# ------------------------------------------------------------------------------------------------------------- #
# deployment script: deploy a version at master, setup develop for the next version.                            #
# For suggestions and errors contact reinhard.budde at iais.fraunhofer.de                                       #
#                                                                                                               #
# For deployments, two parameters are mandatory (both WITHOUT SNAPSHOT)                                         #
# 1. the version number to be deployed on master                                                                #
# 2. the next version to be set in develop (the script suffixes this with "-SNAPSHOT" internally)               #
# E.g. ./deploy.sh 2.4.1 2.5.0                                                                                  #
#                                                                                                               #
# ASSUMPTIONS:                                                                                                  #
# - maven project, may be hierarchical (parent + modules). mvn is used and must be on the PATH!                 #
# - commits to master and develop can be done in a local repo first and then pushed to remote.                  #
# - master and develop branches are clean. Develop is a descendant of master.                                   #
# - develop is checked out                                                                                      #
#                                                                                                               #
# ACTIONS:                                                                                                      #
# - Develop is merged into master.                                                                              #
# - In master the version to be deployed is set. A tag is defined with the name of the deployment version.      #
# - In develop the next SNAPSHOT version is set.                                                                #
# - develop is checked out                                                                                      #
# We want to avoid merge commits and keep a straight line of commits in master as long as possible.             #
#                                                                                                               #
# After the deploy script is run, check for sanity and then push develop and master IMMEDIATELY :-).            #
# ------------------------------------------------------------------------------------------------------------- #
'
# remember script directory, working directory and directory with (parent) pom.
SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CWD=$(pwd)
PARENT="${CWD}"

echo "script directory is ${SCRIPT}, working directory is ${CWD}, parent pom in ${PARENT}"
source ${SCRIPT}/helper/__githelper.sh

# ACK PRECONDITIONS:
question 'if a database upgrade is needed: is class DbUpgrader.java and the SQL script "create-tables.sql" uptodate?'
question 'are the versions for the EV3 update process in RobotEV3/pom.xml (e.g. <ev3runtime.v0.version>) ok?' 

# CONSISTENCY CHECKS:
mavenOnPath                                   # 1. mvn is on the path.
checkMavenBuildDir ${PARENT}                    # 2. pom.xml is found for maven builds.
checkGitProjectDir                            # 3. is a git project.
checkBranch develop                           # 4. we are in branch develop.
checkBranchClean master                       # 5. develop and master are clean.
checkBranchClean develop
parent2child master develop                   # 6. develop is an descendant of master.
thisVersion=$1                                # 7. the version parameter are set and valid
versionStringIsValid "${thisVersion}" "deploy"
nextVersion=$2
versionStringIsValid "${nextVersion}" "next"
nextVersionSnapshot="${nextVersion}-SNAPSHOT"

# the workflow: this version -> develop; merge develop into master; next version-snapshot to develop
git checkout develop
${SCRIPT}/helper/_bumpVersion.sh "${PARENT}" "${thisVersion}" "deployment of version ${thisVersion}"
git checkout master
git merge develop
if [ $? -ne 0 ]
then
	echo 'when merging develop into master a merge conflicht occurred. Why? Ask for help - exit 12'
	git merge --abort
	exit 12
fi
git tag "ORA-${thisVersion}" -m "ORA-${thisVersion}"
git checkout develop
${SCRIPT}/helper/_bumpVersion.sh "${PARENT}" "${nextVersionSnapshot}" "next version is planned to be ${nextVersion}"

echo 'everything looks fine. You are in branch develop and should push both develop and master'
echo "you may run ${SCRIPT}/helper/_pushMasterAndDevelop.sh"