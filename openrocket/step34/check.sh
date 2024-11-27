#!/bin/bash

if [ -e /data/workspace/myshixun/step34/error.txt ]; then
    cat /data/workspace/myshixun/step34/error.txt
else
    cat /data/workspace/myshixun/step34/result.txt
fi
