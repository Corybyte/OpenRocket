#!/bin/bash

if [ -e /data/workspace/myshixun/step39/error.txt ]; then
    cat /data/workspace/myshixun/step39/error.txt
else
    cat /data/workspace/myshixun/step39/result.txt
fi
