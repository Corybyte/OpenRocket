#!/bin/bash

if [ -e /data/workspace/myshixun/step18/error.txt ]; then
    cat /data/workspace/myshixun/step18/error.txt
else
    cat /data/workspace/myshixun/step18/result.txt
fi
