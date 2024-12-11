#!/bin/bash

if [ -e /data/workspace/myshixun/stageMOI/error.txt ]; then
    cat /data/workspace/myshixun/stageMOI/error.txt
else
    cat /data/workspace/myshixun/stageMOI/result.txt
fi
