#!/bin/bash

if [ -e /data/workspace/myshixun/step53/error.txt ]; then
    cat /data/workspace/myshixun/step53/error.txt
else
    cat /data/workspace/myshixun/step53/result.txt
fi
