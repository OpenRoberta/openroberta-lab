echo '
# --------------------------------------------------------------------------------------------------------------------- #
# hotfix script: merge a hotfix to master, merge to develop, setup develop for the next version.                        #
# For suggestions and errors contact reinhard.budde at iais.fraunhofer.de                                               #
#                                                                                                                       #
# For deployments, two parameters are mandatory (both WITHOUT SNAPSHOT)                                                 #
# 1. the version number of the hotfix to be deployed at master (likely the version from develop without -SNAPSHOT)      #
# 2. the next version to be set in develop (the script suffixes this with "-SNAPSHOT" internally)                       #
# E.g. ./hotfix.sh 4.0.2 4.0.3                                                                                          #
#                                                                                                                       #
# ASSUMPTIONS:                                                                                                          #
# - maven project, may be hierarchical (parent + modules). mvn is used and on the PATH!                                 #
# - commits to master and develop can be done in a local repo first and then pushed to remote.                          #
# - the hotfix branch is a descendant of master. Probably only a few commits apart from master.                         #
# - the develop branch is a descendant of master. As many commits as needed apart from master.                          #
# - hotfix, master and develop branches are clean.                                                                      #
# - hotfix is checked out                                                                                               #
# - THE HOTFIX COMMITS ARE WELL TESTED AND MVN CLEAN INSTALL HAS RUN SUCCESSFULLY                                       #
#                                                                                                                       #
# ACTIONS:                                                                                                              #
# - the hotfix branch is merged into master.                                                                            #
# - In master the version to be deployed is set. A tag is defined with the name of the deployment version.              #
# - the master branch is merged into develop.                                                                           #
# - In develop the next SNAPSHOT version is set.                                                                        #
# - develop is checked out                                                                                              #
# Two merge commits are enforced to document the fact, that a hotfix is deployed.                                       #
#                                                                                                                       #
# After the hotfix script is run, check for sanity and then push develop and master to the remote IMMEDIATELY :-).      #
# --------------------------------------------------------------------------------------------------------------------- #
'
# remember script directory, working directory and directory with (parent) pom.
SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CWD=$(pwd)
PARENT="${CWD}"

echo "script directory is ${SCRIPT}, working directory is ${CWD}, parent pom in ${PARENT}"
source ${SCRIPT}/helper/__githelper.sh

# ACK PRECONDITIONS:
question 'is a database upgrade necessary? Did you change the class DbUpgrader.java and the SQL script "create-tables.sql" if needed?'
question 'is an update of versions for the EV3 robots in RobotEV3/pom.xml (e.g. <ev3runtime.v0.version>) needed?' 

# CONSISTENCY CHECKS:
mavenOnPath                               # 1. mvn is on the path.
checkMavenBuildDir ${PARENT}                # 2. pom.xml is found for maven builds.
checkGitProjectDir                        # 3. is a git project.
HOTFIX=$(git rev-parse --abbrev-ref HEAD) # 4. we are in a hotfix branch. This may neither be master nor develop.
if [ "${HOTFIX}" = 'develop' -o "${HOTFIX}" = 'master' ];
then
	echo "this script doesn't run with master or develop checked out - exit 12"
	exit 12
fi
echo "the name of the hotfix branch is ${HOTFIX}"
checkBranchClean master                   # 5. hotfix, develop and master are clean.
checkBranchClean develop
checkBranchClean ${HOTFIX}
parent2child master develop               # 6. hotfix and develop are descendant of master.
parent2child master ${HOTFIX}
thisVersion=$1                            # 7. the version parameter are set and valid
versionStringIsValid "${thisVersion}" "deploy"
nextVersion=$2
versionStringIsValid "${nextVersion}" "next"
nextVersionSnapshot="${nextVersion}-SNAPSHOT"

# the workflow: merge hotfix into master; set the target version in master; set the target version in develop (temporarily);
# merge master into develop; set next version snapshot to develop
git checkout master
git merge --no-ff ${HOTFIX}
${SCRIPT}/helper/_bumpVersion.sh "${PARENT}" "${thisVersion}" "hotfix version ${thisVersion}"
git tag "ORA-${thisVersion}" -m "hotfix version ${thisVersion}"

git checkout develop
${SCRIPT}/helper/_bumpVersion.sh "${PARENT}" "${thisVersion}" "preparing integration of hotfix (version ${thisVersion})"
git merge --no-ff master
if [ $? -ne 0 ]
then
	echo 'when merging master into develop a merge conflicht occurred. Merge is ABORTED - exit 12'
	git merge --abort
	echo 'Run the following commands to re-create the conflicts, solve and commit them:'
	echo 'git merge --no-ff master'
	echo '<<solve the conflicts>>'
	echo 'git add --all; git commit -m "<<sensible message>>"'
	echo "${SCRIPT}/helper/_bumpVersion.sh ${PARENT} ${nextVersionSnapshot} \"next version is planned to be ${nextVersion}\""
	exit 12
fi
${SCRIPT}/helper/_bumpVersion.sh "${PARENT}" "${nextVersionSnapshot}" "next version is planned to be ${nextVersion}"

echo 'everything looks fine. You are in branch develop and should push both develop and master.'
echo "you may run ${SCRIPT}/helper/_pushMasterAndDevelop.sh"
echo 'later you may remove the hotfix branch'