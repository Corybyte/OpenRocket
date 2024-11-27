#!/bin/bash

if [ -e /data/workspace/myshixun/step28/error.txt ]; then
    cat /data/workspace/myshixun/step28/error.txt
else
    cat /data/workspace/myshixun/step28/result.txt
fi
