#!/bin/bash

if [ -e /data/workspace/myshixun/launchLugCG/error.txt ]; then
    cat /data/workspace/myshixun/launchLugCG/error.txt
else
    cat /data/workspace/myshixun/launchLugCG/result.txt
fi
