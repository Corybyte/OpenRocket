#!/bin/bash

if [ -e /data/workspace/myshixun/tubeFinsetMOI/error.txt ]; then
    cat /data/workspace/myshixun/tubeFinsetMOI/error.txt
else
    cat /data/workspace/myshixun/tubeFinsetMOI/result.txt
fi
