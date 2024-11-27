#!/bin/bash

if [ -e /data/workspace/myshixun/step33/error.txt ]; then
    cat /data/workspace/myshixun/step33/error.txt
else
    cat /data/workspace/myshixun/step33/result.txt
fi
