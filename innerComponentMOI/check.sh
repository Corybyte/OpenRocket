#!/bin/bash

if [ -e /data/workspace/myshixun/innerComponentMOI/error.txt ]; then
    cat /data/workspace/myshixun/innerComponentMOI/error.txt
else
    cat /data/workspace/myshixun/innerComponentMOI/result.txt
fi
