#!/bin/bash

if [ -e /data/workspace/myshixun/noseConeMOI/error.txt ]; then
    cat /data/workspace/myshixun/noseConeMOI/error.txt
else
    cat /data/workspace/myshixun/noseConeMOI/result.txt
fi
