#!/bin/bash

if [ -e /data/workspace/myshixun/step19/error.txt ]; then
    cat /data/workspace/myshixun/step19/error.txt
else
    cat /data/workspace/myshixun/step19/result.txt
fi
