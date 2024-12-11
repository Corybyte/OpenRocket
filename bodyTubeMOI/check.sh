#!/bin/bash

if [ -e /data/workspace/myshixun/bodyTubeMOI/error.txt ]; then
    cat /data/workspace/myshixun/bodyTubeMOI/error.txt
else
    cat /data/workspace/myshixun/bodyTubeMOI/result.txt
fi
