# Contributing to `IRDC-CCSK`

## Getting started

First, fork the repository on GitHub to your personal account.

Change `$USER` in the examples below to your Github username if they are not the
same.

```bash
git clone https://github.com/cinrc/irdc-ccsk.git && cd irdc-ccsk
git remote add $USER git@github.com:$USER/irdc-ccsk.git
git fetch -av
```

When contributing, the project version must be appropriately incremented inside the `pom.xml` file. We use a semantic
Maj.Min.Patch.Rev system, where:

* Maj = Major feature or system overhaul
* Min = Feature addition
* Patch = Bug fix
* Rev = Revision (code cleanup, minor edits)

## Contribution Flow

This is a rough outline of what a contributor's workflow looks like:

- Create an issue describing the feature/fix
- Create a topic branch from where you want to base your work. See below for titles
- Make commits of logical units.
- Make sure your commit messages are in the proper format (see below).
- **Important**: Run all unit tests (see below) and make sure they all pass before creating a PR.
- Push your changes to a topic branch in your fork of the repository.
- Submit a pull request to `CinRC/IRC-CCSK`.

See [below](#format-of-the-commit-message) for details on commit best practices
and recommendations.

> **Note:** If you are new to Git(hub) check out [Git rebase, squash...oh
> my!](https://www.mgasch.com/2021/05/git-basics/) for more details on how to
> successfully contribute to an open source project.

### Example 1 - Fix a Bug in `IRDC-CCSK`

```bash
git checkout -b issue-<number> main
git add <files>
git commit -m "bugfix: ..." -m "Closes: #<issue-number>"
git push $USER issue-<number>
```

### Example 2 - Add a new feature to `IRDC-CCSK`

```bash
git checkout -b issue-<number> main
git add <files>
git commit -m "Add feature ..." -m "Closes: #<issue-number>"
git push $USER issue-<number>
```

### Example 3 - Add or fix documentation to `IRDC-CCSK`

```bash
git checkout -b issue-<number> main
git add <files>
git commit -m "IRDC-CCSK: Add/Fix documents:<list documents> ..." -m "Closes: #<issue-number>"
git push $USER issue-<number>
```

### Stay in sync with Upstream

When your branch gets out of sync with the main branch, use the
following to update (rebase):

```bash
git checkout issue-<number>
git fetch -a
git rebase main
git push --force-with-lease $USER issue-<number>
```

### Updating Pull Requests

If your PR fails to pass CI or needs changes based on code review, it's ok to
add more commits stating the changes made, e.g. "Address review comments". This
is to assist the reviewer(s) to easily detect and review the recent changes.

In case of small PRs, it's ok to squash and force-push (see further below)
directly instead.

```bash
# incorporate review feedback
git add .
# create a fixup commit which will be merged into your (original) <commit>
git commit --fixup <commit>
git push $USER issue-<number>
```

Be sure to add a comment to the PR indicating your new changes are ready to
review, as Github does not generate a notification when you git push.

Once the review is complete, squash and push your final commit(s):

```bash
# squash all commits into one
# --autosquash will automatically detect and merge fixup commits
git rebase -i --autosquash main
git push --force-with-lease $USER issue-<number>
```

### Code Style

TODO

### Format of the Commit Message

We follow the conventions described in [How to Write a Git Commit
Message](http://chris.beams.io/posts/git-commit/).

### Running CI Checks and Tests

You can run both `mvn test` and `mvn package` from the top level of the
repository.

Running `mvn test` will run all unit tests. `mvn package` will build the code, which incorporates unit tests in the
build.

## Reporting Bugs and Creating Issues

When opening a new issue, try to roughly follow the commit message format
conventions above.

*This file is an adaptation of the one given by VMWare [here](https://github.com/vmware/govmom).*
