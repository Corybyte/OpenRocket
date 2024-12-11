#!/bin/bash

if [ -e /data/workspace/myshixun/launchLugMOI/error.txt ]; then
    cat /data/workspace/myshixun/launchLugMOI/error.txt
else
    cat /data/workspace/myshixun/launchLugMOI/result.txt
fi
