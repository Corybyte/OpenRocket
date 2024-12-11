#!/bin/bash

if [ -e /data/workspace/myshixun/motorPoint/error.txt ]; then
    cat /data/workspace/myshixun/motorPoint/error.txt
else
    cat /data/workspace/myshixun/motorPoint/result.txt
fi
