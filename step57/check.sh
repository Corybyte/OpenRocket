#!/bin/bash

if [ -e /data/workspace/myshixun/step57/error.txt ]; then
    cat /data/workspace/myshixun/step57/error.txt
else
    cat /data/workspace/myshixun/step57/result.txt
fi
