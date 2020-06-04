echo '
# ------------------------------------------------------------------------------ #
# push master and develop to remote                                              #
#                                                                                #
# ASSUMPTIONS:                                                                   #
# - master and develop branches are clean. Develop is descendant of master.      #
# - develop is checked out                                                       #
#                                                                                #
# ACTIONS:                                                                       #
# - push master and develop to remote                                            #
# - develop is checked out                                                       #
# ------------------------------------------------------------------------------ #
'
# remember script directory, working directory and directory with (parent) pom.
SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"
CWD=$(pwd)
echo "script directory is ${SCRIPT}, working directory is ${CWD}"
source ${SCRIPT}/helper/__githelper.sh

# CONSISTENCY CHECKS:
checkGitProjectDir          # 1. is a git project.
checkBranch develop         # 2. we are in branch develop.
checkBranchClean master     # 3. develop and master are clean.
checkBranchClean develop
parent2child master develop # 4. develop is an descendant of master.

question 'Do you really want to push both master and develop to remote?'
git checkout master
git push
git push --tags
git checkout develop
git push
git push --tags