#!/bin/bash

if [ -e /data/workspace/myshixun/finSetCG/error.txt ]; then
    cat /data/workspace/myshixun/finSetCG/error.txt
else
    cat /data/workspace/myshixun/finSetCG/result.txt
fi
