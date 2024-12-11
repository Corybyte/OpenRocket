#!/bin/bash

if [ -e /data/workspace/myshixun/tubeFinsetCP/error.txt ]; then
    cat /data/workspace/myshixun/tubeFinsetCP/error.txt
else
    cat /data/workspace/myshixun/tubeFinsetCP/result.txt
fi
