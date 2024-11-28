#!/bin/bash

if [ -e /data/workspace/myshixun/step21/error.txt ]; then
    cat /data/workspace/myshixun/step21/error.txt
else
    cat /data/workspace/myshixun/step21/result.txt
fi
