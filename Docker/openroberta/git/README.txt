clones of the git repos needed for this setup. Usually at least a clone of https://github.com/OpenRoberta/openroberta-lab.git .

The file "lockfile" is used for sequential access of the git repos. To aquire a lock for ALL git repos to avoid time races when checking
out and building is overcautious, but as the lock is not required often, it is not blocking too much.