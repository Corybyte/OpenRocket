#!/bin/bash

if [ -e /data/workspace/myshixun/step7/error.txt ]; then
    cat /data/workspace/myshixun/step7/error.txt
else
    cat /data/workspace/myshixun/step7/result.txt
fi
