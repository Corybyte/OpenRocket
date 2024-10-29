#!/bin/bash

if [ -e /data/workspace/myshixun/step27/error.txt ]; then
    cat /data/workspace/myshixun/step27/error.txt
else
    cat /data/workspace/myshixun/step27/result.txt
fi
