#!/bin/bash

if [ -e /data/workspace/myshixun/innerTubeMOI/error.txt ]; then
    cat /data/workspace/myshixun/innerTubeMOI/error.txt
else
    cat /data/workspace/myshixun/innerTubeMOI/result.txt
fi
