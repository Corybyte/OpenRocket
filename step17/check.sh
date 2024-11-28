#!/bin/bash

if [ -e /data/workspace/myshixun/step17/error.txt ]; then
    cat /data/workspace/myshixun/step17/error.txt
else
    cat /data/workspace/myshixun/step17/result.txt
fi
