#!/bin/bash

if [ -e /data/workspace/myshixun/transitionMOI/error.txt ]; then
    cat /data/workspace/myshixun/transitionMOI/error.txt
else
    cat /data/workspace/myshixun/transitionMOI/result.txt
fi
