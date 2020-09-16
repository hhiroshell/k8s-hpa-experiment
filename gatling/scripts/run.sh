#!/bin/sh

cd `dirname $0`

SCENARIO=cowweb.cowweb
BASE_URL=http://localhost:8080
USERS=1000
DURATION=60
DESCRIPTION="USERS=$USERS | DURATION=$DURATION"

JAVA_OPTS="-DbaseUrl=$BASE_URL -Dusers=$USERS -Dduring=$DURATION" gatling -s $SCENARIO -rd "$DESCRIPTION"