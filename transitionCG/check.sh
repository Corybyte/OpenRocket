#!/bin/bash

if [ -e /data/workspace/myshixun/transitionCG/error.txt ]; then
    cat /data/workspace/myshixun/transitionCG/error.txt
else
    cat /data/workspace/myshixun/transitionCG/result.txt
fi
