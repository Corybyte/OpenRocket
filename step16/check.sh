#!/bin/bash

if [ -e /data/workspace/myshixun/step16/error.txt ]; then
    cat /data/workspace/myshixun/step16/error.txt
else
    cat /data/workspace/myshixun/step16/result.txt
fi
