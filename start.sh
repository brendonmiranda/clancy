#!/bin/sh

java -DOWNER=$1 -DTOKEN=$2 -DPREFIX=$3 -DBOT_INACTIVITY_TIME=$4 -jar ./app.jar
