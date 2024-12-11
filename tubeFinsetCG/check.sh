#!/bin/bash

if [ -e /data/workspace/myshixun/tubeFinsetCG/error.txt ]; then
    cat /data/workspace/myshixun/tubeFinsetCG/error.txt
else
    cat /data/workspace/myshixun/tubeFinsetCG/result.txt
fi
