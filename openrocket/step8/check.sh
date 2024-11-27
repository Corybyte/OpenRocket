#!/bin/bash

if [ -e /data/workspace/myshixun/step8/error.txt ]; then
    cat /data/workspace/myshixun/step8/error.txt
else
    cat /data/workspace/myshixun/step8/result.txt
fi
