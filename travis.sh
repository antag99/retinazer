#!/bin/bash
# This script is run by Travis CI to generate badges and other junk

if ! [ "$TRAVIS" ]; then
    echo "This script is intended to be run by Travis CI"
    exit 1
fi

# Get the data from the local repo
REVISION=`git rev-parse HEAD`
LINES_OF_CODE=`git ls-files | grep .java$ | xargs cat | wc -l`

# Clone the antag99.github.io repo
git clone https://github.com/antag99/antag99.github.io.git ~/build/antag99.github.io
cd ~/build/antag99.github.io

mkdir loc/
touch retinazer.txt
OLD_LINES_OF_CODE=$(cat loc/retinazer.txt)

# Add credentials for Antag99 Robot
git config user.name "Anton Gustafsson Bot"
git config user.email "antag99bot@gmail.com"
git config credential.helper "store --file=.git/credentials"
printf "%s" "https://${GH_TOKEN}:@github.com" > .git/credentials

# Update lines of code badge
if [ "$LINES_OF_CODE" != "$OLD_LINES_OF_CODE" ]; then
    echo "$LINES_OF_CODE" > loc/retinazer.txt
    wget https://img.shields.io/badge/lines_of_code-${LINES_OF_CODE}-orange.svg -O loc/retinazer.svg
    git add loc/retinazer.txt
    git add loc/retinazer.svg
    git commit -m "Update lines of code for retinazer/$REVISION"
fi

git push origin master
rm .git/credentials

cd ~/build/retinazer
