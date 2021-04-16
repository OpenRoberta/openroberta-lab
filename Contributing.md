# Contributing Guidelines

We want to make it as simple as possible for our users and contributors to reach us and communicate with one another, so that the Open Roberta Lab can continue to improve. Here are our key channels right now; we'd love to hear from you on one of them.  

## Google Group

[Open Roberta Google Group](https://groups.google.com/g/open-roberta)  

If you have a general inquiry about the Open Roberta Lab, the Open Roberta Google Group is a good place to start. It's also a good place to express the roadmap or upcoming new features.  

## Gitter

[Open Roberta Lab - Gitter](https://gitter.im/openroberta-lab/community)  

For our developer community, we use Gitter. You are welcome to join the group.

## Github

[Open Roberta Issues](https://github.com/OpenRoberta/openroberta-lab/issues)  

Please file an issue in our main GitHub project (OpenRoberta/robertalab) if you find a bug.

The Open Roberta Google Group (see above) is probably a better place to start if you want to brainstorm a potential new feature.

## Development 

This document provides a collection of instructions that will assist you with the contribution process.
We gratefully accept all contributions from anyone who wishes to improve/add new functionality to this project or resolve bugs.

Below you will find the process and workflow for contributing to Open Roberta lab.

## Step 1 : Find an issue
- Take a look at the Existing Issues or create your **own** Issues!
- Ask for the Issue to be assigned to you after which you can start working on it.
- Note : Every change in this project should/must have an associated issue. 

## Step 2 : Fork the Project
- Fork this Repository. This will create a Local Copy of this Repository on your Github Profile. Keep a reference to the original project in `upstream` remote.
.
- Development happens in the [develop branch](https://github.com/OpenRoberta/openroberta-lab/tree/develop). Please send PRs against that branch.
```
$ git clone https://github.com/<Your username>/openroberta-lab.git
$ cd openroberta-lab
$ git remote add upstream https://github.com/OpenRoberta/openroberta-lab.git
```

- If you have already forked the project, update your copy before working.
```
$ git remote update
$ git checkout <branch-name>
$ git rebase upstream/<branch-name>
```
## Step 3 : Branch
Create a new branch. Use its name to identify the issue you are addressing.
```
# It will create a new branch with name Branch_Name and switch to that branch

$ git checkout -b branch_name
```
## Step 4 : Work on the issue assigned
- Work on the issue(s) assigned to you. 
- Add all the files/folders needed.
- After you've made changes or made your contribution to the project add changes to the branch you've just created by:
```
# To add all new files to branch Branch_Name

$ git add .
```
## Step 5 : Commit
- A good commit message focuses on the `why` instead of the `what`.
```
# This message get associated with all files you have changed

$ git commit -m "message"
```
## Step 6 : Work Remotely
- Now you are ready to your work to the remote repository.
- When your work is ready and complies with the project conventions, upload your changes to your fork:

```
# To push your work to your remote repository

$ git push -u origin Branch_Name
```

## Step 7 : Pull Request
- Go to your repository in browser and click on compare and pull requests. Then add a title and description to your pull request that explains your contribution.

- Voila! Your Pull Request has been submitted and will be reviewed by the moderators/admins/project manger.

You can follow the test status [here](https://travis-ci.org/github/OpenRoberta).

## Need more help?
You can refer to the following articles on basics of Git and Github and also contact the Project Mentors, in case you are stuck:
- [Forking a Repo](https://help.github.com/en/github/getting-started-with-github/fork-a-repo)
- [Cloning a Repo](https://help.github.com/en/desktop/contributing-to-projects/creating-an-issue-or-pull-request)
- [How to create a Pull Request](https://opensource.com/article/19/7/create-pull-request-github)
- [Getting started with Git and GitHub](https://towardsdatascience.com/getting-started-with-git-and-github-6fcd0f2d4ac6)
- [Learn GitHub from Scratch](https://lab.github.com/githubtraining/introduction-to-github)

