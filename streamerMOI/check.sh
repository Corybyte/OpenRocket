#!/bin/bash

if [ -e /data/workspace/myshixun/streamerMOI/error.txt ]; then
    cat /data/workspace/myshixun/streamerMOI/error.txt
else
    cat /data/workspace/myshixun/streamerMOI/result.txt
fi
