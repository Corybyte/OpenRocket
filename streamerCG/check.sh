#!/bin/bash

if [ -e /data/workspace/myshixun/streamerCG/error.txt ]; then
    cat /data/workspace/myshixun/streamerCG/error.txt
else
    cat /data/workspace/myshixun/streamerCG/result.txt
fi
