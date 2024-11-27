#!/bin/bash

if [ -e /data/workspace/myshixun/step1/error.txt ]; then
    cat /data/workspace/myshixun/step1/error.txt
else
    cat /data/workspace/myshixun/step1/result.txt
fi
