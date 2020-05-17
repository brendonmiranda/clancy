#!/bin/sh

java -DOWNER=$1 -DTOKEN=$2 -DPREFIX=$3 -jar ./app.jar
