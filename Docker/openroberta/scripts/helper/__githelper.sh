# helper functions - MUST BE SOURCED

# ask a question. If the user answers "y", everything is fine. Otherwise exit 12
function question {
	echo -n "$1 (\"y\" if ok) "
	local ANSWER
	read ANSWER
	case "${ANSWER}" in
	  y) : ;;
	  *) exit 12 ;;
	esac
}

# check whether mvn is on the path. If not exit 12
function mavenOnPath {
	local MVN=$(mvn -version)
	if [ -z "${MVN}" ]
	then
		echo "mvn is not on the path. Build is impossible - exit 12"
		exit 12
	else
		MVN=$(echo "${MVN}"|head -1);
		echo "using ${MVN}"
	fi
}

# check whether the current working dir has a .git directory. If not exit 12
function checkGitProjectDir {
	if [ -d .git ]
	then
		:
	else
		echo "this script only runs in a git directory - exit 12"
		exit 12
	fi
}

# check whether we have a pom.xml in the build directory (given as $1). If not exit 12
function checkMavenBuildDir {
	local BUILD=$1
	if [ -d "${BUILD}" -a -f "${BUILD}/pom.xml" ]
	then
		:
	else
		echo "\"${BUILD}\" is no valid directory for maven builds - exit 12"
		exit 12
	fi
}

# return the name of the branch we are in. Get it with: local B = $(getBranchName)
function getBranchName {
	local BRANCH=$(git rev-parse --abbrev-ref HEAD)
	echo "${BRANCH}"
	return 0
}

# check whether we are in a branch given in $1. If not exit 12
function checkBranch {
	local EXPECTED=$1
	local BRANCH=$(git rev-parse --abbrev-ref HEAD)
	if [ "${BRANCH}" != "${EXPECTED}" ];
	then
		echo "this script expects branch ${EXPECTED}, but we are in ${BRANCH} - exit 12"
		exit 12
	fi
}

# check whether a branch given in $1 is parent of branch given in $2. If not exit 12
function parent2child {
	local PARENT=$1
	local CHILD=$2
	git merge-base --is-ancestor master develop
	if [ $? -ne 0 ]
	then
		echo "${PARENT} IS NO ANCESTOR OF ${CHILD}. Solve this problem - exit 12"
		exit 12
	fi
}

# check whether the branch (given in $1) is clean. If not exit 12
function checkBranchClean {
	local BRANCH=$1
	git checkout ${BRANCH}
	if [ $? -ne 0 ]
	then
		echo "checkout of branch ${BRANCH} failed - exit 12"
		exit 12
	fi
	if [ -z "$(git status --porcelain)" ];
	then
		:
	else
		echo "please commit your changes in branch ${BRANCH} first. Exit 12"
		exit 12
	fi
}

# check whether the version string (given in $1) is not empty and without -SNAPSHOT. If not exit 12
# a hint what the version is used for is given in $2 (for an error message)
function versionStringIsValid {
	local VERSION=$1
	local HINT=$2
	case "${VERSION}" in
	  '')         echo "${HINT} version is missing - exit 12"
				  exit 12 ;;
	  *-SNAPSHOT) echo 'snapshot version not legal here (remove -SNAPSHOT) - exit 12'
				  exit 12 ;;
	  *)          echo "${HINT} version will bumped to version ${VERSION}" ;;
	esac
}
