#!/bin/bash

if [ -e /data/workspace/myshixun/finSetMOI/error.txt ]; then
    cat /data/workspace/myshixun/finSetMOI/error.txt
else
    cat /data/workspace/myshixun/finSetMOI/result.txt
fi
