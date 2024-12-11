#!/bin/bash

if [ -e /data/workspace/myshixun/massComponentMOI/error.txt ]; then
    cat /data/workspace/myshixun/massComponentMOI/error.txt
else
    cat /data/workspace/myshixun/massComponentMOI/result.txt
fi
