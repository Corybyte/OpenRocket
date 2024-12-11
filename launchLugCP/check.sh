#!/bin/bash

if [ -e /data/workspace/myshixun/launchLugCP/error.txt ]; then
    cat /data/workspace/myshixun/launchLugCP/error.txt
else
    cat /data/workspace/myshixun/launchLugCP/result.txt
fi
