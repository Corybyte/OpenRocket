#!/bin/bash

if [ -e /data/workspace/myshixun/step55/error.txt ]; then
    cat /data/workspace/myshixun/step55/error.txt
else
    cat /data/workspace/myshixun/step55/result.txt
fi
