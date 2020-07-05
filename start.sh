#!/bin/sh

java -DOWNER=$1 -DTOKEN=$2 -DPREFIX=$3 -DINACTIVE_THRESHOLD=$4 -jar ./app.jar
