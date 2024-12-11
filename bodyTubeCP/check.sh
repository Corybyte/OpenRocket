#!/bin/bash

if [ -e /data/workspace/myshixun/bodyTubeCP/error.txt ]; then
    cat /data/workspace/myshixun/bodyTubeCP/error.txt
else
    cat /data/workspace/myshixun/bodyTubeCP/result.txt
fi
