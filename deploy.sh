#!/bin/bash

REMOTE=`git config remote.origin.url`
git remote set-url --push origin ${REMOTE/#git:/https:}
git remote set-branches --add origin gh-pages
git fetch

## Deploy artifacts to ~/maven
mvn deploy

## Find current version to display in commit message
VERSION=$(mvn -q \
    -Dexec.executable="echo" \
    -Dexec.args='${project.version}' \
    --non-recursive \
    org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
SNAPSHOT=`echo ${VERSION} | grep -- -SNAPSHOT`
REVISION=`git rev-parse HEAD`

## Discard files changed locally
git reset --hard
git clean -dffx .

## Checkout gh-pages branch and copy the artifacts
git checkout gh-pages
## Remove old snapshot versions
rm -rfv ./maven/**/*/*-SNAPSHOT/
cp -rf ~/maven .

if [ $SNAPSHOT ]
then
    # Use commit hash in the message
    VERSION=$REVISION
fi

## Commit changes
git config user.name ${GIT_NAME}
git config user.email ${GIT_EMAIL}
git config credential.helper "store --file=.git/credentials"
printf "%s" "https://${GH_TOKEN}:@github.com" > .git/credentials

git add --all .
git commit -m "Upload artifacts for ${VERSION}"
git push origin gh-pages
rm .git/credentials

## Switch back to master branch
git checkout master
